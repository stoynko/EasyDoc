package com.github.stoynko.easydoc.web.controllers;

import com.github.stoynko.easydoc.security.UserAuthenticationDetails;
import com.github.stoynko.easydoc.web.dto.DtoContext;
import com.github.stoynko.easydoc.web.resolvers.RoleViewResolverRegistry;
import com.github.stoynko.easydoc.web.utilities.PageBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import static com.github.stoynko.easydoc.web.model.ViewPage.PRESCRIPTIONS_TABLE;

@Controller
@RequiredArgsConstructor
public class PrescriptionsController {

    private final RoleViewResolverRegistry viewResolverRegistry;
    private final PageBuilder pageBuilder;

    @GetMapping("/prescriptions")
    @PreAuthorize(value = "hasAnyRole('PATIENT', 'DOCTOR')")
    public ModelAndView getPrescriptionsPage(@AuthenticationPrincipal UserAuthenticationDetails principal) {
        return pageBuilder.buildPage(DtoContext.forPage(PRESCRIPTIONS_TABLE, principal));
    }

}
