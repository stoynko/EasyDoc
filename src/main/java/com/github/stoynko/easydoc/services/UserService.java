package com.github.stoynko.easydoc.services;

import com.github.stoynko.easydoc.events.EmailChangeEvent;
import com.github.stoynko.easydoc.events.RegistrationCompletedEvent;
import com.github.stoynko.easydoc.events.UserContextRefreshEvent;
import com.github.stoynko.easydoc.exceptions.CredentialsException;
import com.github.stoynko.easydoc.exceptions.ErrorMessages;
import com.github.stoynko.easydoc.exceptions.InvalidInputException;
import com.github.stoynko.easydoc.exceptions.UserDoesNotExistException;
import com.github.stoynko.easydoc.exceptions.UserExistsWithEmailException;
import com.github.stoynko.easydoc.exceptions.UserExistsWithPinException;
import com.github.stoynko.easydoc.models.EmailVerificationToken;
import com.github.stoynko.easydoc.models.User;
import com.github.stoynko.easydoc.models.enums.AccountAuthority;
import com.github.stoynko.easydoc.models.enums.AccountRole;
import com.github.stoynko.easydoc.models.enums.AccountStatus;
import com.github.stoynko.easydoc.repositories.UserRepository;
import com.github.stoynko.easydoc.security.UserAuthenticationDetails;
import com.github.stoynko.easydoc.web.dto.request.*;
import com.github.stoynko.easydoc.web.mappers.EntityMapper;
import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.github.stoynko.easydoc.exceptions.ErrorMessages.ACCOUNT_DUPLICATE_EMAIL;
import static com.github.stoynko.easydoc.exceptions.ErrorMessages.ACCOUNT_NOT_FOUND;
import static com.github.stoynko.easydoc.exceptions.ErrorMessages.CREDENTIALS_PASSWORD_INVALID;
import static com.github.stoynko.easydoc.models.enums.AccountAuthority.CAN_BOOK_APPOINTMENT;
import static com.github.stoynko.easydoc.models.enums.AccountAuthority.CAN_SUBMIT_PRACTITIONER_APPLICATION;
import static com.github.stoynko.easydoc.models.enums.AccountRole.ADMIN;
import static com.github.stoynko.easydoc.models.enums.AccountRole.DOCTOR;
import static com.github.stoynko.easydoc.models.enums.AccountStatus.ACTIVE;
import static com.github.stoynko.easydoc.models.enums.AccountStatus.DELETED;
import static com.github.stoynko.easydoc.models.enums.AccountStatus.EMAIL_UNVERIFIED;
import static com.github.stoynko.easydoc.models.enums.AccountStatus.INCOMPLETE;
import static com.github.stoynko.easydoc.models.enums.AccountStatus.SUSPENDED;
import static com.github.stoynko.easydoc.utilities.ValidationUtilities.normalizeEmailAddress;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final EmailVerificationService emailVerificationService;
    private final ApplicationEventPublisher eventPublisher;

    public static final String ACCOUNT_DELETION_CONFIGURATION = "I confirm the deletion of my account";

    @Transactional
    public void registerAccount(@Valid RegisterRequest registerRequest) {

        String emailAddress = normalizeEmailAddress(registerRequest.getEmailAddress());
        if (repository.existsByEmailAddress(emailAddress)) {
            throw new UserExistsWithEmailException();
        }

        String hashedPassword = passwordEncoder.encode(registerRequest.getPassword());
        User user = EntityMapper.toUserEntity(registerRequest, hashedPassword);

        repository.save(user);
        log.info("-registration | emailAddress:{} timestamp:{}", registerRequest.getEmailAddress(), LocalDateTime.now());

        EmailVerificationToken verificationToken = emailVerificationService.createTokenForUser(user);
        eventPublisher.publishEvent(new RegistrationCompletedEvent(
                user.getId(), user.getEmailAddress(), verificationToken.getId()));
    }

    public void verifyEmailAddress(UUID tokenId) {

        EmailVerificationToken token = emailVerificationService.getValidToken(tokenId);
        User user = token.getUser();

        user.setEmailVerified(true);
        user.getAuthority().add(CAN_BOOK_APPOINTMENT);
        user.getAuthority().add(CAN_SUBMIT_PRACTITIONER_APPLICATION);

        updateAccountStatus(user);

        emailVerificationService.markTokenAsUsed(token);
        eventPublisher.publishEvent(new UserContextRefreshEvent(user.getId()));
        log.info("-emailVerification | userId: {} timestamp:{}", user.getId(), LocalDateTime.now());
    }

    @Transactional
    public void updateAccountStatus(User user) {
        if (user.getAccountStatus() == SUSPENDED || user.getAccountStatus() == DELETED) {
            return;
        }

        if (!user.isProfileCompleted()) {
            user.setAccountStatus(INCOMPLETE);
        } else if (!user.isEmailVerified()) {
            user.setAccountStatus(EMAIL_UNVERIFIED);
        } else {
            user.setAccountStatus(ACTIVE);
        }
    }

    @Transactional
    public void updateAccountStatusAndSave(User user) {
        updateAccountStatus(user);
        repository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String email = normalizeEmailAddress(username);
        User user = repository.findByEmailAddress(email).orElseThrow(() ->
                new UsernameNotFoundException(ACCOUNT_NOT_FOUND.getErrorMessage()));

        return new UserAuthenticationDetails(user.getId(), user.getEmailAddress(),
                user.getPasswordHash(),user.getAccountStatus(), user.getRole(), user.getAuthority());
    }

    @Transactional
    public void submitPersonalInfo(UUID uuid, SubmitAccountDetailsRequest request) {
        User user = getUserById(uuid);

        if (repository.existsUserByPersonalIdentificationNumber(request.getPin())) {
            throw new UserExistsWithPinException();
        }
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPersonalIdentificationNumber(request.getPin());
        user.setDateOfBirth(request.getDateOfBirth());
        user.setGender(request.getGender());
        user.setProfileCompleted(true);
        updateAccountStatus(user);

        repository.save(user);
        log.info("-submitPersonalInfo | userId: {} timestamp:{}", user.getId(), LocalDateTime.now());

        eventPublisher.publishEvent(new UserContextRefreshEvent(user.getId()));
    }

    public void updateEmailAddress(UUID uuid, UpdateEmailAddressRequest request) {

        User user = getUserById(uuid);

        if (user.getEmailAddress().equals(request.getNewEmailAddress())) {
            throw new UserExistsWithEmailException();
        }

        if (repository.existsByEmailAddress(request.getNewEmailAddress())) {
            throw new UserExistsWithEmailException();
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new CredentialsException();
        }

        user.setEmailAddress(request.getNewEmailAddress());
        user.setEmailVerified(false);
        updateAccountStatus(user);
        repository.save(user);

        EmailVerificationToken verificationToken = emailVerificationService.createTokenForUser(user);
        eventPublisher.publishEvent(new EmailChangeEvent
                (user.getId(), user.getEmailAddress(), verificationToken.getId()));

        log.info("-updateEmailAddress | userId: {} timestamp:{}", user.getId(), LocalDateTime.now());
    }

    public void updatePassword(UUID uuid, UpdatePasswordRequest request) {
        User user = getUserById(uuid);

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPasswordHash())) {
            throw new CredentialsException();
        }

        if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
            throw new CredentialsException();
        }

        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        repository.save(user);
        log.info("-updatePassword | userId: {} timestamp:{}", user.getId(), LocalDateTime.now());
    }

    public void updateAccountStatus(UUID uuid, AccountStatus status) {

        User user = getUserById(uuid);

        if (user.getAccountStatus() == status) {
            return;
        }
        user.setAccountStatus(status);
        repository.save(user);
        log.info("-accountStatusChange | userId: {} timestamp:{}", user.getId(), LocalDateTime.now());
    }

    public void updateUserRoleTo(User user, AccountRole role) {
        user.setRole(role);
        repository.save(user);
        log.info("-roleChange | userId: {} timestamp:{}", user.getId(), LocalDateTime.now());
    }

    public void updatePersonalInfo(UUID uuid, UpdateAccountDetailsRequest request) {
        User user = getUserById(uuid);
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setDateOfBirth(request.getDateOfBirth());
        user.setGender(request.getGender());
        repository.save(user);
        log.info("-updatePersonalInfo | userId: {} timestamp:{}", user.getId(), LocalDateTime.now());
    }

    public User getUserById(UUID uuid) {
        return repository.findById(uuid).orElseThrow(() -> new UserDoesNotExistException());
    }

    public void updateContactDetails(UUID uuid, UpdateContactDetailsRequest request) {
        User user = getUserById(uuid);
        user.setPhoneNumber(request.getPhoneNumber());
        repository.save(user);
        log.info("-updateContactDetails | userId: {} timestamp:{}", user.getId(), LocalDateTime.now());
    }

    public void deleteAccount(UUID uuid, DeleteAccountRequest request) {
        User user = getUserById(uuid);

        if (!passwordEncoder.matches(request.getPasswordConfirmation(), user.getPasswordHash())) {
            throw new CredentialsException();
        }

        if (!ACCOUNT_DELETION_CONFIGURATION.equals(request.getConfirmationInput())) {
            throw new InvalidInputException();
        }

        updateAccountStatus(user.getId(), DELETED);
        repository.save(user);
        log.info("-accountDeleted | userId: {} timestamp:{}", user.getId(), LocalDateTime.now());
    }

    public List<User> getUsersByRole(AccountRole role) {
        return repository.findAllByRole(role);
    }

    public void revokeAuthority(UUID uuid, AccountAuthority authority) {
        User user = getUserById(uuid);
        user.getAuthority().remove(authority);
        repository.save(user);
        log.info("-authorityRevoked | userId: {} authority: {} timestamp:{}",
                user.getId(), authority.name(), LocalDateTime.now());
    }

    public void reinstateAuthority(UUID uuid, AccountAuthority authority) {
        User user = getUserById(uuid);
        user.getAuthority().add(authority);
        repository.save(user);
        log.info("-authorityReinstated | userId: {} authority: {} timestamp:{}",
                user.getId(), authority.name(), LocalDateTime.now());
    }

    public List<User> getAllUsersExceptAdmins() {
        return repository.findAllByRoleNot(ADMIN);
    }

    public int getTotalUsersCountExceptAdmins() {
        return repository.countUserByRoleNot(ADMIN);
    }

    public int getDoctorsCount() {
        return repository.countByRole(DOCTOR);
    }

    public List<User> getAllUsersByRole(AccountRole role) {
       return (role == null) ? getAllUsersExceptAdmins() : getUsersByRole(role);
    }
}
