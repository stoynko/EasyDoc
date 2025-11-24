package com.github.stoynko.easydoc.web.controllers;

import com.github.stoynko.easydoc.models.enums.AccountRole;
import com.github.stoynko.easydoc.models.enums.Expertise;
import com.github.stoynko.easydoc.security.UserAuthenticationDetails;
import com.github.stoynko.easydoc.services.PractitionerApplicationService;
import com.github.stoynko.easydoc.services.UserService;
import com.github.stoynko.easydoc.web.dto.response.DoctorBriefSummaryResponse;
import com.github.stoynko.easydoc.web.dto.response.UserSummaryResponse;
import com.github.stoynko.easydoc.web.mappers.DtoMapper;
import com.github.stoynko.easydoc.web.utilities.PageBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.github.stoynko.easydoc.models.enums.AccountStatus.ACTIVE;
import static com.github.stoynko.easydoc.models.enums.AccountStatus.SUSPENDED;
import static com.github.stoynko.easydoc.web.dto.DtoContext.forPage;
import static com.github.stoynko.easydoc.web.dto.DtoContext.forTargetResource;
import static com.github.stoynko.easydoc.web.model.ViewPage.DOCTORS;
import static com.github.stoynko.easydoc.web.model.ViewPage.PRACTITIONER_APPLICATIONS;
import static com.github.stoynko.easydoc.web.model.ViewPage.USERS;

@Controller
public class AdminController {

    private final PageBuilder pageBuilder;
    private final UserService userService;
    private final PractitionerApplicationService practitionerApplicationService;

    @Autowired
    public AdminController(PageBuilder pageBuilder, UserService userService, PractitionerApplicationService practitionerApplicationService) {
        this.pageBuilder = pageBuilder;
        this.userService = userService;
        this.practitionerApplicationService = practitionerApplicationService;
    }

    @GetMapping("/users")
    @PreAuthorize(value = "hasRole('ADMIN')")
    public ModelAndView getUsersPage(@AuthenticationPrincipal UserAuthenticationDetails principal) {
        return pageBuilder.buildPage(forPage(USERS, principal));
    }

    @GetMapping(value = "/users", params = "role")
    public ModelAndView getUsersByRole(@RequestParam(value = "role", required = false) AccountRole role,
                                             @AuthenticationPrincipal UserAuthenticationDetails principal) {

        List<UserSummaryResponse> users = userService.getAllUsersByRole(role)
                .stream().map(DtoMapper::toUserSummaryFrom)
                .collect(Collectors.toList());

        ModelAndView modelAndView = pageBuilder.buildPage(forPage(USERS, principal));
        modelAndView.addObject("users", users);
        modelAndView.addObject("userRole", role);
        return modelAndView;
    }

    @PostMapping("/users/{uuid}/enable")
    @PreAuthorize(value = "hasRole('ADMIN')")
    public ModelAndView enableAccount(@AuthenticationPrincipal UserAuthenticationDetails principal,
                                      @PathVariable UUID uuid) {
        userService.updateAccountStatus(uuid, ACTIVE);
        return new ModelAndView("redirect:/users");
    }

    @PostMapping("/users/{uuid}/suspend")
    @PreAuthorize(value = "hasRole('ADMIN')")
    public ModelAndView suspendAccount(@AuthenticationPrincipal UserAuthenticationDetails principal,
                                    @PathVariable UUID uuid) {
        userService.updateAccountStatus(uuid, SUSPENDED);
        return new ModelAndView("redirect:/users");
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
