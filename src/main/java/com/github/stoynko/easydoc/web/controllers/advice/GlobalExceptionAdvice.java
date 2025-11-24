package com.github.stoynko.easydoc.web.controllers.advice;

import com.github.stoynko.easydoc.exceptions.AlreadyOnboardedException;
import com.github.stoynko.easydoc.exceptions.InvalidTokenException;
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
    public ModelAndView handleUserWithPinAlreadyExistsException(UserExistsWithPinException exception,
                                                                @AuthenticationPrincipal UserAuthenticationDetails principal) {
        ModelAndView modelAndView = pageBuilder.buildPage(forPage(ERROR, principal));
        modelAndView.addObject("errorMessage", "onboardingAccountInfoSubmitFailure");
        modelAndView.addObject("errorMessageDetails", exception.getMessage());
        return modelAndView;
    }

    @ExceptionHandler(AlreadyOnboardedException.class)
    public ModelAndView handleAlreadyOnboardedException(AlreadyOnboardedException exception,
                                                        @AuthenticationPrincipal UserAuthenticationDetails principal) {
        ModelAndView modelAndView = pageBuilder.buildPage(forPage(DOCTOR_ONBOARDING, principal));
        modelAndView.addObject("processState", "failed");
        modelAndView.addObject("errorMessageDetails", exception.getMessage());
        return modelAndView;
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ModelAndView handleInvalidTokenException(InvalidTokenException exception) {
        ModelAndView modelAndView = pageBuilder.buildPage(forPage(ERROR, null));
        modelAndView.addObject("errorMessage", "emailVerificationFailure");
        modelAndView.addObject("errorMessageDetails", exception.getMessage());
        return modelAndView;
    }
    //TODO: Handle 413 - MaxUploadSizeExceededException when uploading avatar with larger size
}
