package com.github.stoynko.easydoc.user.web;

import com.github.stoynko.easydoc.security.UserAuthenticationDetails;
import com.github.stoynko.easydoc.user.model.AccountAuthority;
import com.github.stoynko.easydoc.user.service.UserService;
import com.github.stoynko.easydoc.web.utilities.PageBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

import static com.github.stoynko.easydoc.user.model.AccountStatus.ACTIVE;
import static com.github.stoynko.easydoc.user.model.AccountStatus.SUSPENDED;
import static com.github.stoynko.easydoc.web.dto.DtoContext.forPage;
import static com.github.stoynko.easydoc.web.model.ViewPage.USERS;

@Controller
@RequiredArgsConstructor
public class AccountAdminController {

    private final PageBuilder pageBuilder;
    private final UserService userService;

    @GetMapping("/users")
    @PreAuthorize(value = "hasRole('ADMIN')")
    public ModelAndView getUsersPage(@AuthenticationPrincipal UserAuthenticationDetails principal) {
        return pageBuilder.buildPage(forPage(USERS, principal));
    }

    @PostMapping("/users/{userId}/enable")
    @PreAuthorize(value = "hasRole('ADMIN')")
    public ModelAndView enableAccount(@AuthenticationPrincipal UserAuthenticationDetails principal,
                                      @PathVariable UUID userId) {
        userService.updateAccountStatus(userId, ACTIVE);
        return pageBuilder.buildPage(forPage(USERS, principal));
    }

    @PostMapping("/users/{userId}/suspend")
    @PreAuthorize(value = "hasRole('ADMIN')")
    public ModelAndView suspendAccount(@AuthenticationPrincipal UserAuthenticationDetails principal,
                                       @PathVariable UUID userId) {
        userService.updateAccountStatus(userId, SUSPENDED);
        return pageBuilder.buildPage(forPage(USERS, principal));
    }

    @PostMapping("/users/{userId}/authorities/revoke/{authority}")
    @PreAuthorize(value = "hasRole('ADMIN')")
    public ModelAndView revokeAuthority(@AuthenticationPrincipal UserAuthenticationDetails principal,
                                        @PathVariable UUID userId,
                                        @PathVariable AccountAuthority authority) {
        userService.revokeAuthority(userId, authority);
        return pageBuilder.buildPage(forPage(USERS, principal));
    }

    @PostMapping("/users/{userId}/authorities/reinstate/{authority}")
    @PreAuthorize(value = "hasRole('ADMIN')")
    public ModelAndView reinstateAuthority(@AuthenticationPrincipal UserAuthenticationDetails principal,
                                           @PathVariable UUID userId,
                                           @PathVariable AccountAuthority authority) {
        userService.reinstateAuthority(userId, authority);
        return pageBuilder.buildPage(forPage(USERS, principal));
    }
}
