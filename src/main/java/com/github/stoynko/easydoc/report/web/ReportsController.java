package com.github.stoynko.easydoc.report.web;

import com.github.stoynko.easydoc.security.UserAuthenticationDetails;

import com.github.stoynko.easydoc.web.resolvers.RoleViewResolverRegistry;
import com.github.stoynko.easydoc.web.utilities.PageBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import static com.github.stoynko.easydoc.web.dto.DtoContext.forPage;
import static com.github.stoynko.easydoc.web.model.ViewPage.MEDICAL_REPORTS_TABLE;
import static com.github.stoynko.easydoc.web.model.ViewPage.MEDICAL_REPORT_VIEW;

@Controller
@RequiredArgsConstructor
public class ReportsController {

    private final RoleViewResolverRegistry viewResolverRegistry;
    private final PageBuilder pageBuilder;

    @GetMapping("/reports")
    public ModelAndView getReportsPage(@AuthenticationPrincipal UserAuthenticationDetails principal) {
        return pageBuilder.buildPage(forPage(MEDICAL_REPORTS_TABLE, principal));
    }
}
