package com.github.stoynko.easydoc.web.controllers;

import com.github.stoynko.easydoc.models.enums.AccountAuthority;
import com.github.stoynko.easydoc.security.UserAuthenticationDetails;
import com.github.stoynko.easydoc.services.PractitionerApplicationService;
import com.github.stoynko.easydoc.services.UserService;
import com.github.stoynko.easydoc.web.utilities.PageBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

import static com.github.stoynko.easydoc.models.enums.AccountStatus.ACTIVE;
import static com.github.stoynko.easydoc.models.enums.AccountStatus.SUSPENDED;
import static com.github.stoynko.easydoc.web.dto.DtoContext.forPage;
import static com.github.stoynko.easydoc.web.model.ViewPage.PRACTITIONER_APPLICATIONS;
import static com.github.stoynko.easydoc.web.model.ViewPage.USERS;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final PageBuilder pageBuilder;
    private final UserService userService;
    private final PractitionerApplicationService practitionerApplicationService;

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

    @GetMapping("/applications")
    @PreAuthorize(value = "hasRole('ADMIN')")
    public ModelAndView getApplicationsPage(@AuthenticationPrincipal UserAuthenticationDetails principal) {
        return pageBuilder.buildPage(forPage(PRACTITIONER_APPLICATIONS, principal));
    }

    @PostMapping("/applications/{uuid}/approve")
    @PreAuthorize(value = "hasRole('ADMIN')")
    public ModelAndView approvePractitionerApplication(@AuthenticationPrincipal UserAuthenticationDetails principal,
                                                       @PathVariable UUID uuid) {

        practitionerApplicationService.approveApplication(uuid);
        return new ModelAndView("redirect:/applications");
    }

    @PostMapping("/applications/{uuid}/reject")
    @PreAuthorize(value = "hasRole('ADMIN')")
    public ModelAndView rejectPractitionerApplication(@AuthenticationPrincipal UserAuthenticationDetails principal,
                                                      @PathVariable UUID uuid) {

        practitionerApplicationService.rejectApplication(uuid);
        return new ModelAndView("redirect:/applications");
    }
}
