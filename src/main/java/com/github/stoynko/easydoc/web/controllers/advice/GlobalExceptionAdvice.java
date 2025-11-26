package com.github.stoynko.easydoc.web.controllers.advice;

import com.github.stoynko.easydoc.exceptions.AccountIncompleteException;
import com.github.stoynko.easydoc.exceptions.AlreadyOnboardedException;
import com.github.stoynko.easydoc.exceptions.ApplicationAlreadyExistsException;
import com.github.stoynko.easydoc.exceptions.BookingSuspendedException;
import com.github.stoynko.easydoc.exceptions.EmailNotVerifiedException;
import com.github.stoynko.easydoc.exceptions.InvalidTokenException;
import com.github.stoynko.easydoc.exceptions.MissingAuthorityException;
import com.github.stoynko.easydoc.exceptions.UserExistsWithEmailException;
import com.github.stoynko.easydoc.exceptions.UserExistsWithPinException;
import com.github.stoynko.easydoc.security.UserAuthenticationDetails;
import com.github.stoynko.easydoc.web.utilities.PageBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import static com.github.stoynko.easydoc.web.dto.DtoContext.forPage;
import static com.github.stoynko.easydoc.web.model.ViewPage.ACCOUNT_ONBOARDING;
import static com.github.stoynko.easydoc.web.model.ViewPage.DOCTOR_ONBOARDING;
import static com.github.stoynko.easydoc.web.model.ViewPage.ERROR;

@ControllerAdvice
public class GlobalExceptionAdvice {

    private final PageBuilder pageBuilder;

    @Autowired
    public GlobalExceptionAdvice(PageBuilder pageBuilder) {
        this.pageBuilder = pageBuilder;
    }

    @ExceptionHandler(UserExistsWithEmailException.class)
    public ModelAndView handleEmailAlreadyExistsException(UserExistsWithEmailException exception) {
        ModelAndView modelAndView = pageBuilder.buildPage(forPage(ERROR, null));
        modelAndView.addObject("errorMessage", "registrationFailed");
        modelAndView.addObject("errorMessageDetails", exception.getMessage());
        return modelAndView;
    }

    @ExceptionHandler(UserExistsWithPinException.class)
    public ModelAndView handleUserWithPinAlreadyExistsException(@AuthenticationPrincipal UserAuthenticationDetails principal,
                                                                UserExistsWithPinException exception) {
        ModelAndView modelAndView = pageBuilder.buildPage(forPage(ERROR, principal));
        modelAndView.addObject("errorMessage", "onboardingAccountInfoSubmitFailure");
        modelAndView.addObject("errorMessageDetails", exception.getMessage());
        return modelAndView;
    }

    @ExceptionHandler(AlreadyOnboardedException.class)
    public ModelAndView handleAlreadyOnboardedException(@AuthenticationPrincipal UserAuthenticationDetails principal,
                                                        AlreadyOnboardedException exception) {
        ModelAndView modelAndView = pageBuilder.buildPage(forPage(DOCTOR_ONBOARDING, principal));
        modelAndView.addObject("processState", "failed");
        modelAndView.addObject("errorMessageDetails", exception.getMessage());
        return modelAndView;
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ModelAndView handleInvalidTokenException(InvalidTokenException exception) {
        ModelAndView modelAndView = pageBuilder.buildPage(forPage(ERROR, null));
        modelAndView.addObject("errorMessage", "tokenVerificationFailure");
        modelAndView.addObject("errorMessageDetails", exception.getMessage());
        return modelAndView;
    }

    @ExceptionHandler(MissingAuthorityException.class)
    public ModelAndView handleMissingAuthorityException(@AuthenticationPrincipal UserAuthenticationDetails principal,
                                                        MissingAuthorityException exception) {
        ModelAndView modelAndView = pageBuilder.buildPage(forPage(ERROR, principal));
        modelAndView.addObject("errorMessage", "authorityCheckFailure");
        modelAndView.addObject("errorMessageDetails", exception.getMessage());
        return modelAndView;
    }

    @ExceptionHandler(EmailNotVerifiedException.class)
    public ModelAndView handleEmailNotVerifiedException(@AuthenticationPrincipal UserAuthenticationDetails principal,
                                                        EmailNotVerifiedException exception) {
        ModelAndView modelAndView = pageBuilder.buildPage(forPage(ERROR, principal));
        modelAndView.addObject("errorMessage", "emailVerificationCheckFailure");
        modelAndView.addObject("errorMessageDetails", exception.getMessage());
        return modelAndView;
    }

    @ExceptionHandler(BookingSuspendedException.class)
    public ModelAndView handleBookingSuspendedException (@AuthenticationPrincipal UserAuthenticationDetails principal,
                                                         BookingSuspendedException exception) {
        ModelAndView modelAndView = pageBuilder.buildPage(forPage(ERROR, principal));
        modelAndView.addObject("errorMessage", "canBookCheckFailure");
        modelAndView.addObject("errorMessageDetails", exception.getMessage());
        return modelAndView;
    }

    @ExceptionHandler(ApplicationAlreadyExistsException.class)
    public ModelAndView handleApplicationAlreadyExistsException (@AuthenticationPrincipal UserAuthenticationDetails principal,
                                                         ApplicationAlreadyExistsException exception) {
        ModelAndView modelAndView = pageBuilder.buildPage(forPage(ACCOUNT_ONBOARDING, principal));
        return modelAndView;
    }

    @ExceptionHandler(AccountIncompleteException.class)
    public ModelAndView handleAccountIncompleteException (@AuthenticationPrincipal UserAuthenticationDetails principal,
                                                          AccountIncompleteException exception) {
        ModelAndView modelAndView = pageBuilder.buildPage(forPage(ERROR, principal));
        modelAndView.addObject("errorMessage", "canSubmitApplicationCheckFailure");
        modelAndView.addObject("errorMessageDetails", exception.getMessage());
        return modelAndView;
    }
    //TODO: Handle 413 - MaxUploadSizeExceededException when uploading avatar with larger size
}


/*UnsatisfiedServletRequestParameterException*/