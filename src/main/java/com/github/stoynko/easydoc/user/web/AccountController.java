package com.github.stoynko.easydoc.user.web;

import com.github.stoynko.easydoc.security.UserAuthenticationDetails;
import com.github.stoynko.easydoc.practitioner.service.DoctorService;
import com.github.stoynko.easydoc.user.model.User;
import com.github.stoynko.easydoc.user.service.UserService;
import com.github.stoynko.easydoc.web.dto.DtoContext;
import com.github.stoynko.easydoc.user.web.dto.request.DeleteAccountRequest;
import com.github.stoynko.easydoc.user.web.dto.request.SubmitAccountDetailsRequest;
import com.github.stoynko.easydoc.user.web.dto.request.UpdateAccountDetailsRequest;
import com.github.stoynko.easydoc.user.web.dto.request.UpdateContactDetailsRequest;
import com.github.stoynko.easydoc.user.web.dto.request.UpdateEmailAddressRequest;
import com.github.stoynko.easydoc.user.web.dto.request.UpdatePasswordRequest;
import com.github.stoynko.easydoc.practitioner.web.dto.request.UpdateProfessionalDetailsRequest;
import com.github.stoynko.easydoc.practitioner.web.dto.request.UpdateProfilePhotoRequest;
import com.github.stoynko.easydoc.web.utilities.PageBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.UUID;

import static com.github.stoynko.easydoc.web.dto.DtoContext.forFragment;
import static com.github.stoynko.easydoc.web.dto.DtoContext.forPage;
import static com.github.stoynko.easydoc.web.model.ViewFragment.DELETE_ACCOUNT;
import static com.github.stoynko.easydoc.web.model.ViewFragment.MEMBERSHIP;
import static com.github.stoynko.easydoc.web.model.ViewFragment.NONE;
import static com.github.stoynko.easydoc.web.model.ViewFragment.PERSONAL_INFO;
import static com.github.stoynko.easydoc.web.model.ViewFragment.PROFESSIONAL_INFO;
import static com.github.stoynko.easydoc.web.model.ViewFragment.SECURITY;
import static com.github.stoynko.easydoc.web.model.ViewPage.ACCOUNT;
import static com.github.stoynko.easydoc.web.model.ViewPage.ACCOUNT_ONBOARDING;
import static com.github.stoynko.easydoc.web.model.ViewPage.CONFIRMATION;
import static com.github.stoynko.easydoc.web.model.ViewPage.NOTIFICATIONS;
import static com.github.stoynko.easydoc.web.model.ViewPage.SETTINGS;

@Controller
@RequiredArgsConstructor
public class AccountController {

    private final PageBuilder pageBuilder;
    private final UserService userService;
    private final DoctorService doctorService;

    @GetMapping("/onboarding/account")
    public ModelAndView getOnboardingAccountPage(@AuthenticationPrincipal UserAuthenticationDetails principal) {

        if (userService.getUserById(principal.getId()).isProfileCompleted()) {
            return new ModelAndView("redirect:/");
        }

        ModelAndView modelAndView = pageBuilder.buildPage(forPage(ACCOUNT_ONBOARDING, principal));
        return modelAndView;
    }

    @PostMapping("/onboarding/account")
    public ModelAndView submitOnboardingInformation(@AuthenticationPrincipal UserAuthenticationDetails principal,
                                                    @Valid SubmitAccountDetailsRequest request,
                                                    BindingResult bindingResult) {

        if (userService.getUserById(principal.getId()).isProfileCompleted()) {
            return new ModelAndView("redirect:/");
        }

        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = pageBuilder.buildPage(forPage(ACCOUNT_ONBOARDING, principal));
            return pageBuilder.addErrors(modelAndView, "submitPersonalDetailsRequest", request, bindingResult);
        }

        userService.submitPersonalInfo(principal.getId(), request);
        ModelAndView modelAndView = pageBuilder.buildPage(forPage(ACCOUNT_ONBOARDING, principal));
        modelAndView.addObject("processState", "success");
        return modelAndView;
    }

    @GetMapping("/auth/verify-email")
    public ModelAndView verifyEmail(@RequestParam("token") UUID tokenId,
                                    @AuthenticationPrincipal UserAuthenticationDetails principal) {

        userService.verifyEmailAddress(tokenId);
        ModelAndView modelAndView = pageBuilder.buildPage(DtoContext.forPage(CONFIRMATION, principal));
        return modelAndView.addObject("confirmationMessage", "emailVerificationSuccess");
    }

    @GetMapping("/account")
    public ModelAndView getAccountPage(@AuthenticationPrincipal UserAuthenticationDetails principal) {
        return pageBuilder.buildPage(forPage(ACCOUNT, principal));
    }

    @GetMapping("/settings/personal-info")
    public ModelAndView getPersonalInfoPage(@AuthenticationPrincipal UserAuthenticationDetails principal) {
        return pageBuilder.buildPage(forFragment(SETTINGS, PERSONAL_INFO, principal));
    }

    @PostMapping("/settings/personal-info/photo")
    @PreAuthorize("hasRole('DOCTOR')")
    public ModelAndView changeAvatar(@AuthenticationPrincipal UserAuthenticationDetails principal,
                                     @Valid UpdateProfilePhotoRequest request,
                                     BindingResult bindingResult) throws IOException {

        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = pageBuilder.buildPage(forFragment(SETTINGS, PERSONAL_INFO, principal));
            return pageBuilder.addErrors(modelAndView, "changeProfilePhotoRequest", request, bindingResult);
        }

        doctorService.updateUserProfilePhoto(principal.getId(), request);
        ModelAndView modelAndView = pageBuilder.buildPage(forPage(CONFIRMATION, principal));
        return modelAndView.addObject("confirmationMessage", "profilePhotoUpdateSuccess");
    }

    @PostMapping("/settings/personal-info/update")
    public ModelAndView changePersonalInfo(@AuthenticationPrincipal UserAuthenticationDetails principal,
                                           @Valid UpdateAccountDetailsRequest request,
                                           BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = pageBuilder.buildPage(forFragment(SETTINGS, PERSONAL_INFO, principal));
            return pageBuilder.addErrors(modelAndView, "changePersonalDetailsRequest", request, bindingResult);
        }

        userService.updatePersonalInfo(principal.getId(), request);

        ModelAndView modelAndView = pageBuilder.buildPage(forPage(CONFIRMATION, principal));
        return modelAndView.addObject("confirmationMessage", "personalInfoUpdateSuccess");

    }

    @PostMapping("/settings/personal-info/contacts")
    public ModelAndView changeContactInfo(@AuthenticationPrincipal UserAuthenticationDetails principal,
                                          @Valid UpdateContactDetailsRequest request,
                                          BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = pageBuilder.buildPage(forFragment(SETTINGS, PERSONAL_INFO, principal));
            return pageBuilder.addErrors(modelAndView, "changeContactDetailsRequest", request, bindingResult);
        }

        userService.updateContactDetails(principal.getId(), request);

        ModelAndView modelAndView = pageBuilder.buildPage(forPage(CONFIRMATION, principal));
        return modelAndView.addObject("confirmationMessage", "contactInfoUpdateSuccess");

    }

    @GetMapping("/settings/professional-info")
    @PreAuthorize(value = "hasRole('DOCTOR')")
    public ModelAndView getProfessionalInfoPage(@AuthenticationPrincipal UserAuthenticationDetails principal) {
        return pageBuilder.buildPage(forFragment(SETTINGS, PROFESSIONAL_INFO, principal));
    }

    @PostMapping("/settings/professional-info")
    @PreAuthorize(value = "hasRole('DOCTOR')")
    public ModelAndView changeProfessionalInfo(@AuthenticationPrincipal UserAuthenticationDetails principal,
                                               @Valid UpdateProfessionalDetailsRequest request,
                                               BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = pageBuilder.buildPage(forFragment(SETTINGS, PROFESSIONAL_INFO, principal));
            return pageBuilder.addErrors(modelAndView, "changeProfessionalDetailsRequest", request, bindingResult);
        }

        doctorService.changeProfessionalDetails(principal.getId(), request);
        ModelAndView modelAndView = pageBuilder.buildPage(forPage(CONFIRMATION, principal));
        return modelAndView.addObject("confirmationMessage", "professionalInfoUpdateSuccess");
    }

    @GetMapping("/settings/security")
    public ModelAndView getSecurityPage(@AuthenticationPrincipal UserAuthenticationDetails principal) {
        return pageBuilder.buildPage(forFragment(SETTINGS, SECURITY, principal));
    }

    @PostMapping("/settings/security/email")
    public ModelAndView changeEmailAddress(@AuthenticationPrincipal UserAuthenticationDetails principal,
                                           @Valid UpdateEmailAddressRequest request,
                                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            if (request.getCurrentEmailAddress() != null && request.getNewEmailAddress() != null &&
                    request.getCurrentEmailAddress().trim().equalsIgnoreCase(request.getNewEmailAddress().trim())) {

                bindingResult.rejectValue(
                        "newEmailAddress", "email.same", "The new email address must be different from current email address"
                );
            }
            ModelAndView modelAndView = pageBuilder.buildPage(forFragment(SETTINGS, SECURITY, principal));
            return pageBuilder.addErrors(modelAndView, "changeEmailAddressRequest", request, bindingResult);
        }

        userService.updateEmailAddress(principal.getId(), request);
        ModelAndView modelAndView = pageBuilder.buildPage(forPage(CONFIRMATION, principal));
        return modelAndView.addObject("confirmationMessage", "emailAddressUpdateSuccess");
    }

    @PostMapping("/settings/security/password")
    public ModelAndView changePassword(@AuthenticationPrincipal UserAuthenticationDetails principal,
                                       @Valid UpdatePasswordRequest request,
                                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = pageBuilder.buildPage(forFragment(SETTINGS, SECURITY, principal));
            return pageBuilder.addErrors(modelAndView, "changePasswordRequest", request, bindingResult);
        }
        userService.updatePassword(principal.getId(), request);
        ModelAndView modelAndView = pageBuilder.buildPage(forPage(CONFIRMATION, principal));
        return modelAndView.addObject("confirmationMessage", "passwordUpdateSuccess");
    }

    @GetMapping("/settings/preferences")
    public ModelAndView getPreferencesPage(@AuthenticationPrincipal UserAuthenticationDetails principal) {
        return pageBuilder.buildPage(forFragment(NOTIFICATIONS, NONE, principal));
    }

    @GetMapping("/settings/delete")
    @PreAuthorize("hasAnyRole('PATIENT', 'DOCTOR')")
    public ModelAndView getDeleteAccountPage(@AuthenticationPrincipal UserAuthenticationDetails principal) {
        return pageBuilder.buildPage(forFragment(SETTINGS, DELETE_ACCOUNT, principal));
    }

    @PostMapping("/settings/delete")
    @PreAuthorize("hasAnyRole('PATIENT', 'DOCTOR'," + "@securityCheck.isAccountActive(#principal))")
    public ModelAndView deleteAccount(@AuthenticationPrincipal UserAuthenticationDetails principal,
                                      @Valid DeleteAccountRequest request,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = pageBuilder.buildPage(forFragment(SETTINGS, DELETE_ACCOUNT, principal));
            return pageBuilder.addErrors(modelAndView, "deleteAccountRequest", request, bindingResult);
        }
        userService.deleteAccount(principal.getId(), request);
        ModelAndView modelAndView = pageBuilder.buildPage(forPage(CONFIRMATION, principal));
        return modelAndView.addObject("confirmationMessage", "deleteRequestSuccess");
    }
}
