package com.github.stoynko.easydoc.practitioner.web;

import com.github.stoynko.easydoc.security.UserAuthenticationDetails;
import com.github.stoynko.easydoc.practitioner.service.PractitionerApplicationService;
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

import static com.github.stoynko.easydoc.web.dto.DtoContext.forPage;
import static com.github.stoynko.easydoc.web.model.ViewPage.PRACTITIONER_APPLICATIONS;

@Controller
@RequiredArgsConstructor
public class ApplicationAdminController {

    private final PageBuilder pageBuilder;
    private final UserService userService;
    private final PractitionerApplicationService practitionerApplicationService;

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
