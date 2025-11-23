package com.github.stoynko.easydoc.web.controllers;

import com.github.stoynko.easydoc.security.UserAuthenticationDetails;
import com.github.stoynko.easydoc.web.dto.DtoContext;
import com.github.stoynko.easydoc.web.resolvers.RoleViewResolverRegistry;
import com.github.stoynko.easydoc.web.utilities.PageBuilder;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import static com.github.stoynko.easydoc.web.model.ViewPage.NOTIFICATIONS;

@Controller
public class NotificationsController {

    private final RoleViewResolverRegistry roleViewResolverRegistry;
    private final PageBuilder pageBuilder;

    public NotificationsController(RoleViewResolverRegistry roleViewResolverRegistry, PageBuilder pageBuilder) {
        this.roleViewResolverRegistry = roleViewResolverRegistry;
        this.pageBuilder = pageBuilder;
    }

    @GetMapping("/notifications")
    public ModelAndView getNotificationsPage(@AuthenticationPrincipal UserAuthenticationDetails principal) {
        return pageBuilder.buildPage(DtoContext.forPage(NOTIFICATIONS, principal));
    }
}
