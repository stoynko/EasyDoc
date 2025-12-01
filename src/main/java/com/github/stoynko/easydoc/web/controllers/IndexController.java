package com.github.stoynko.easydoc.web.controllers;

import com.github.stoynko.easydoc.security.UserAuthenticationDetails;
import com.github.stoynko.easydoc.practitioner.service.PractitionerApplicationService;
import com.github.stoynko.easydoc.user.service.UserService;
import com.github.stoynko.easydoc.user.web.dto.request.RegisterRequest;
import com.github.stoynko.easydoc.web.handler.SecurityCheck;
import com.github.stoynko.easydoc.web.utilities.PageBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import static com.github.stoynko.easydoc.web.dto.DtoContext.forFragment;
import static com.github.stoynko.easydoc.web.dto.DtoContext.forPage;
import static com.github.stoynko.easydoc.web.model.ViewFragment.SETTINGS_DASHBOARD;
import static com.github.stoynko.easydoc.web.model.ViewPage.CONFIRMATION;
import static com.github.stoynko.easydoc.web.model.ViewPage.DASHBOARD;
import static com.github.stoynko.easydoc.web.model.ViewPage.INDEX;
import static com.github.stoynko.easydoc.web.model.ViewPage.LOGIN;
import static com.github.stoynko.easydoc.web.model.ViewPage.REGISTER;
import static com.github.stoynko.easydoc.web.model.ViewPage.SETTINGS;

@Controller
@RequiredArgsConstructor
public class IndexController {

    private final UserService userService;
    private final PractitionerApplicationService applicationService;
    private final PageBuilder pageBuilder;
    private final SecurityCheck securityCheck;

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

    @GetMapping("/dashboard")
    public ModelAndView getDashboardPage(@AuthenticationPrincipal UserAuthenticationDetails principal) {
        return pageBuilder.buildPage(forPage(DASHBOARD, principal));
    }

    @GetMapping({"/settings", "/settings/dashboard"})
    public ModelAndView getSettingsDashboardPage(@AuthenticationPrincipal UserAuthenticationDetails principal) {
        return pageBuilder.buildPage(forFragment(SETTINGS, SETTINGS_DASHBOARD, principal));
    }
}
