package com.github.stoynko.easydoc.web.controllers;

import com.github.stoynko.easydoc.security.UserAuthenticationDetails;
import com.github.stoynko.easydoc.services.PractitionerApplicationService;
import com.github.stoynko.easydoc.services.UserService;
import com.github.stoynko.easydoc.web.utilities.PageBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import static com.github.stoynko.easydoc.web.dto.DtoContext.forPage;
import static com.github.stoynko.easydoc.web.model.ViewPage.ACCOUNT;
import static com.github.stoynko.easydoc.web.model.ViewPage.DOCTORS;

@Controller
public class DoctorController {

    private final PageBuilder pageBuilder;
    private final PractitionerApplicationService applicationService;
    private final UserService userService;

    @Autowired
    public DoctorController(PageBuilder pageBuilder, PractitionerApplicationService applicationService, UserService userService) {
        this.pageBuilder = pageBuilder;
        this.applicationService = applicationService;
        this.userService = userService;
    }

    @GetMapping("/account")
    public ModelAndView getAccountPage(@AuthenticationPrincipal UserAuthenticationDetails principal) {
        return pageBuilder.buildPage(forPage(ACCOUNT, principal));
    }

    @GetMapping("/doctors")
    public ModelAndView getDoctorsPage(@AuthenticationPrincipal UserAuthenticationDetails principal) {
        return pageBuilder.buildPage(forPage(DOCTORS, principal));
    }
}
