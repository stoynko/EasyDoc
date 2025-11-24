package com.github.stoynko.easydoc.web.controllers;

import com.github.stoynko.easydoc.security.UserAuthenticationDetails;

import com.github.stoynko.easydoc.web.dto.DtoContext;
import com.github.stoynko.easydoc.web.resolvers.RoleViewResolverRegistry;
import com.github.stoynko.easydoc.web.utilities.PageBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import static com.github.stoynko.easydoc.web.dto.DtoContext.forPage;
import static com.github.stoynko.easydoc.web.model.ViewPage.MEDICAL_REPORTS;

@Controller
public class ReportsController {

    private final RoleViewResolverRegistry viewResolverRegistry;
    private final PageBuilder pageBuilder;

    @Autowired
    public ReportsController(RoleViewResolverRegistry viewResolverRegistry, PageBuilder pageBuilder) {
        this.viewResolverRegistry = viewResolverRegistry;
        this.pageBuilder = pageBuilder;
    }

    @GetMapping("/reports")
    public ModelAndView getReportsPage(@AuthenticationPrincipal UserAuthenticationDetails principal) {
        return pageBuilder.buildPage(forPage(MEDICAL_REPORTS, principal));
    }

    @GetMapping("/report")
    public ModelAndView getReportCreation(@AuthenticationPrincipal UserAuthenticationDetails principal) {
        return pageBuilder.buildPage(forPage(MEDICAL_REPORTS, principal));
    }
}
