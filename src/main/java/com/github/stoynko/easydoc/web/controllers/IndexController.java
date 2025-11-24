package com.github.stoynko.easydoc.web.controllers;

import com.github.stoynko.easydoc.security.UserAuthenticationDetails;
import com.github.stoynko.easydoc.services.PractitionerApplicationService;
import com.github.stoynko.easydoc.services.UserService;
import com.github.stoynko.easydoc.web.dto.request.SubmitAccountDetailsRequest;
import com.github.stoynko.easydoc.web.dto.request.RegisterPractitionerRequest;
import com.github.stoynko.easydoc.web.dto.request.RegisterRequest;
import com.github.stoynko.easydoc.web.model.ViewPage;
import com.github.stoynko.easydoc.web.utilities.PageBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

import static com.github.stoynko.easydoc.web.dto.DtoContext.forFragment;
import static com.github.stoynko.easydoc.web.dto.DtoContext.forPage;
import static com.github.stoynko.easydoc.web.model.ViewFragment.DOCTOR_LANDING;
import static com.github.stoynko.easydoc.web.model.ViewFragment.DOCTOR_ONBOARDING;
import static com.github.stoynko.easydoc.web.model.ViewPage.ACCOUNT_ONBOARDING;
import static com.github.stoynko.easydoc.web.model.ViewPage.CONFIRMATION;
import static com.github.stoynko.easydoc.web.model.ViewPage.DASHBOARD;
import static com.github.stoynko.easydoc.web.model.ViewPage.INDEX;
import static com.github.stoynko.easydoc.web.model.ViewPage.LOGIN;
import static com.github.stoynko.easydoc.web.model.ViewPage.REGISTER;

@Controller
public class IndexController {

    private final UserService userService;
    private final PractitionerApplicationService applicationService;
    private final PageBuilder pageBuilder;

    @Autowired
    public IndexController(UserService userService, PractitionerApplicationService applicationService, PageBuilder pageBuilder) {
        this.userService = userService;
        this.applicationService = applicationService;
        this.pageBuilder = pageBuilder;
    }

    @GetMapping("/")
    public ModelAndView getIndexPage(@AuthenticationPrincipal UserAuthenticationDetails principal) {

        if (principal != null) {
            return pageBuilder.buildPage(forPage(DASHBOARD, principal));
        }

        ModelAndView modelAndView = new ModelAndView(INDEX.getPage());
        return modelAndView;

    }

    @GetMapping("/register")
    public ModelAndView getRegisterPage() {
        return pageBuilder.buildPage(forPage(REGISTER, null));
    }

    @PostMapping("/register")
    public ModelAndView registerUser(@Valid RegisterRequest registerRequest,
                                     BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = pageBuilder.buildPage(forPage(REGISTER, null));
            return pageBuilder.addErrors(modelAndView, "registerRequest", registerRequest, bindingResult);
        }

        userService.registerAccount(registerRequest);
        ModelAndView modelAndView = pageBuilder.buildPage(forPage(CONFIRMATION, null));
        return modelAndView.addObject("confirmationMessage", "registrationSuccess");
    }

    @GetMapping("/login")
    public ModelAndView getLoginPage(HttpServletRequest httpRequest) {
        ModelAndView modelAndView = pageBuilder.buildPage(forPage(LOGIN, null));

        HttpSession session = httpRequest.getSession();
        if (session != null) {
            String error = (String) session.getAttribute("LOGIN_ERROR");
            if (error != null) {
                modelAndView.addObject("loginError", error);
                session.removeAttribute("LOGIN_ERROR");
            }
        }

        return modelAndView;
    }

    @GetMapping("/onboarding/account")
    public ModelAndView getOnboardingAccountPage(@AuthenticationPrincipal UserAuthenticationDetails principal) {
        ModelAndView modelAndView = pageBuilder.buildPage(forPage(ACCOUNT_ONBOARDING, principal));
        return modelAndView;
    }

    @PostMapping("/onboarding/account")
    public ModelAndView submitOnboardingInformation(@AuthenticationPrincipal UserAuthenticationDetails principal,
                                                    @Valid SubmitAccountDetailsRequest request,
                                                    BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = pageBuilder.buildPage(forPage(ACCOUNT_ONBOARDING, principal));
            return pageBuilder.addErrors(modelAndView, "submitPersonalDetailsRequest", request, bindingResult);
        }

        userService.submitPersonalInfo(principal.getId(), request);
        ModelAndView modelAndView = pageBuilder.buildPage(forPage(ACCOUNT_ONBOARDING, principal));
        modelAndView.addObject("processState", "success");
        return modelAndView;
    }

    @GetMapping("/onboarding/doctors")
    @PreAuthorize(value = "hasRole('PATIENT')")
    public ModelAndView getOnboardingPage(@AuthenticationPrincipal UserAuthenticationDetails principal) {
        ModelAndView modelAndView = pageBuilder.buildPage(forFragment(ViewPage.DOCTOR_ONBOARDING, DOCTOR_LANDING, principal));
        modelAndView.addObject("processState", "landing");
        return modelAndView;
    }

    @GetMapping("/onboarding/doctors/apply")
    @PreAuthorize(value = "hasRole('PATIENT')")
    public ModelAndView getOnboardingForm(@AuthenticationPrincipal UserAuthenticationDetails principal) {
        return pageBuilder.buildPage(forFragment(ViewPage.DOCTOR_ONBOARDING, DOCTOR_ONBOARDING, principal));
    }

    @PostMapping("/onboarding/doctors/apply")
    @PreAuthorize(value = "hasRole('PATIENT')")
    public ModelAndView submitOnboardingApplication(@AuthenticationPrincipal UserAuthenticationDetails principal,
                                                    @Valid RegisterPractitionerRequest request,
                                                    BindingResult bindingResult) throws IOException {

        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = pageBuilder.buildPage(forFragment(ViewPage.DOCTOR_ONBOARDING, DOCTOR_ONBOARDING, principal));
            return pageBuilder.addErrors(modelAndView, "registerPractitionerRequest", request, bindingResult);
        }

        applicationService.submitApplication(principal.getId(), request);
        ModelAndView modelAndView = pageBuilder.buildPage(forPage(ViewPage.DOCTOR_ONBOARDING, principal));
        modelAndView.addObject("processState", "success");
        return modelAndView;
    }

    @GetMapping("/dashboard")
    public ModelAndView getDashboardPage(@AuthenticationPrincipal UserAuthenticationDetails principal) {
        return pageBuilder.buildPage(forPage(DASHBOARD, principal));
    }
}
