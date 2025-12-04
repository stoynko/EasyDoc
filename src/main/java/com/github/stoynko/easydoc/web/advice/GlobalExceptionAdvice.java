package com.github.stoynko.easydoc.web.advice;

import com.github.stoynko.easydoc.prescription.exception.PrescriptionAlreadyExists;
import com.github.stoynko.easydoc.report.exception.ReportEditAccessDeniedException;
import com.github.stoynko.easydoc.user.exception.AccountIncompleteException;
import com.github.stoynko.easydoc.practitioner.exception.PractitionerAlreadyOnboardedException;
import com.github.stoynko.easydoc.practitioner.exception.PractitionerApplicationAlreadyExistsException;
import com.github.stoynko.easydoc.appointment.exception.BookingSuspendedException;
import com.github.stoynko.easydoc.user.exception.AccountSuspendedException;
import com.github.stoynko.easydoc.user.exception.EmailNotVerifiedException;
import com.github.stoynko.easydoc.shared.exception.InvalidTokenException;
import com.github.stoynko.easydoc.shared.exception.MissingAuthorityException;
import com.github.stoynko.easydoc.user.exception.UserExistsWithEmailException;
import com.github.stoynko.easydoc.user.exception.UserExistsWithPinException;
import com.github.stoynko.easydoc.security.UserAuthenticationDetails;
import com.github.stoynko.easydoc.web.utilities.PageBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import static com.github.stoynko.easydoc.web.dto.DtoContext.forPage;
import static com.github.stoynko.easydoc.web.model.ViewPage.DOCTOR_ONBOARDING;
import static com.github.stoynko.easydoc.web.model.ViewPage.ERROR;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionAdvice {

    private final PageBuilder pageBuilder;

    @ExceptionHandler(UserExistsWithEmailException.class)
    public ModelAndView handleEmailAlreadyExistsException(UserExistsWithEmailException exception) {
        ModelAndView modelAndView = pageBuilder.buildPage(forPage(ERROR, null));
        modelAndView.addObject("errorMessage", "registration_failure");
        modelAndView.addObject("errorMessageDetails", exception.getMessage());
        return modelAndView;
    }

    @ExceptionHandler(UserExistsWithPinException.class)
    public ModelAndView handleUserWithPinAlreadyExistsException(@AuthenticationPrincipal UserAuthenticationDetails principal,
                                                                UserExistsWithPinException exception) {
        ModelAndView modelAndView = pageBuilder.buildPage(forPage(ERROR, principal));
        modelAndView.addObject("errorMessage", "onboarding_account_info_submit_failure");
        modelAndView.addObject("errorMessageDetails", exception.getMessage());
        return modelAndView;
    }

    @ExceptionHandler(AccountSuspendedException.class)
    public ModelAndView handleAccountSuspendedException(@AuthenticationPrincipal UserAuthenticationDetails principal, Exception exception) {
        ModelAndView modelAndView = pageBuilder.buildPage(forPage(ERROR, principal));
        modelAndView.addObject("errorMessage", "account_suspended");
        modelAndView.addObject("errorMessageDetails", exception.getMessage());
        return modelAndView;
    }

    @ExceptionHandler(PractitionerAlreadyOnboardedException.class)
    public ModelAndView handlePractitionerAlreadyOnboardedException(@AuthenticationPrincipal UserAuthenticationDetails principal,
                                                                    PractitionerAlreadyOnboardedException exception) {
        ModelAndView modelAndView = pageBuilder.buildPage(forPage(DOCTOR_ONBOARDING, principal));
        modelAndView.addObject("processState", "failed");
        modelAndView.addObject("errorMessageDetails", exception.getMessage());
        return modelAndView;
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ModelAndView handleInvalidTokenException(InvalidTokenException exception) {
        ModelAndView modelAndView = pageBuilder.buildPage(forPage(ERROR, null));
        modelAndView.addObject("errorMessage", "token_verification_failure");
        modelAndView.addObject("errorMessageDetails", exception.getMessage());
        return modelAndView;
    }

    @ExceptionHandler(EmailNotVerifiedException.class)
    public ModelAndView handleEmailNotVerifiedException(@AuthenticationPrincipal UserAuthenticationDetails principal,
                                                        EmailNotVerifiedException exception) {
        ModelAndView modelAndView = pageBuilder.buildPage(forPage(ERROR, principal));
        modelAndView.addObject("errorMessage", "email_verification_check_failure");
        modelAndView.addObject("errorMessageDetails", exception.getMessage());
        return modelAndView;
    }

    @ExceptionHandler(MissingAuthorityException.class)
    public ModelAndView handleMissingAuthorityException(@AuthenticationPrincipal UserAuthenticationDetails principal,
                                                        MissingAuthorityException exception) {
        ModelAndView modelAndView = pageBuilder.buildPage(forPage(ERROR, principal));
        modelAndView.addObject("errorMessage", "authority_check_failure");
        modelAndView.addObject("errorMessageDetails", exception.getMessage());
        return modelAndView;
    }

    @ExceptionHandler(BookingSuspendedException.class)
    public ModelAndView handleBookingSuspendedException (@AuthenticationPrincipal UserAuthenticationDetails principal,
                                                         BookingSuspendedException exception) {
        ModelAndView modelAndView = pageBuilder.buildPage(forPage(ERROR, principal));
        modelAndView.addObject("errorMessage", "can_book_check_failure");
        modelAndView.addObject("errorMessageDetails", exception.getMessage());
        return modelAndView;
    }

    @ExceptionHandler(PractitionerApplicationAlreadyExistsException.class)
    public ModelAndView handleApplicationAlreadyExistsException (@AuthenticationPrincipal UserAuthenticationDetails principal,
                                                         PractitionerApplicationAlreadyExistsException exception) {
        ModelAndView modelAndView = pageBuilder.buildPage(forPage(ERROR, principal));
        modelAndView.addObject("errorMessage", "application_already_exists");
        modelAndView.addObject("errorMessageDetails", exception.getMessage());
        return modelAndView;
    }

    @ExceptionHandler(AccountIncompleteException.class)
    public ModelAndView handleAccountIncompleteException (@AuthenticationPrincipal UserAuthenticationDetails principal,
                                                          AccountIncompleteException exception) {
        ModelAndView modelAndView = pageBuilder.buildPage(forPage(ERROR, principal));
        modelAndView.addObject("errorMessage", "can_submit_application_check_failure");
        modelAndView.addObject("errorMessageDetails", exception.getMessage());
        return modelAndView;
    }

    @ExceptionHandler(PrescriptionAlreadyExists.class)
    public ModelAndView handlePrescriptionAlreadyExistsException(@AuthenticationPrincipal UserAuthenticationDetails principal,
                                                                 PrescriptionAlreadyExists exception) {
        ModelAndView modelAndView = pageBuilder.buildPage(forPage(ERROR, principal));
        modelAndView.addObject("errorMessage", "prescription_exists");
        modelAndView.addObject("errorMessageDetails", exception.getMessage());
        return modelAndView;
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ModelAndView handleNoResourceFoundException(@AuthenticationPrincipal UserAuthenticationDetails principal, Exception exception) {
        ModelAndView modelAndView = pageBuilder.buildPage(forPage(ERROR, principal));
        modelAndView.addObject("errorMessage", "resource_not_found");
        modelAndView.addObject("errorMessageDetails", exception.getMessage());
        return modelAndView;
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(@AuthenticationPrincipal UserAuthenticationDetails principal, Exception exception) {
        ModelAndView modelAndView = pageBuilder.buildPage(forPage(ERROR, principal));
        modelAndView.addObject("errorMessage", "general_error");
        modelAndView.addObject("errorMessageDetails", exception.getMessage());
        return modelAndView;
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ModelAndView handleMaxUploadSizeExceededException(@AuthenticationPrincipal UserAuthenticationDetails principal) {
        ModelAndView modelAndView = pageBuilder.buildPage(forPage(ERROR, principal));
        modelAndView.addObject("errorMessage", "max_upload_size_exceeded");
        return modelAndView;
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ModelAndView handleAuthorizationDeniedException(@AuthenticationPrincipal UserAuthenticationDetails principal) {
        ModelAndView modelAndView = pageBuilder.buildPage(forPage(ERROR, principal));
        modelAndView.addObject("errorMessage", "access_denied_exception");
        return modelAndView;
    }
}
