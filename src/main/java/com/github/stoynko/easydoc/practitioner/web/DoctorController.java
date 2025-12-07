package com.github.stoynko.easydoc.practitioner.web;

import com.github.stoynko.easydoc.practitioner.model.Expertise;
import com.github.stoynko.easydoc.security.UserAuthenticationDetails;
import com.github.stoynko.easydoc.practitioner.service.DoctorService;
import com.github.stoynko.easydoc.practitioner.service.PractitionerApplicationService;
import com.github.stoynko.easydoc.practitioner.web.dto.request.RegisterPractitionerRequest;
import com.github.stoynko.easydoc.practitioner.web.dto.response.DoctorBriefSummaryResponse;
import com.github.stoynko.easydoc.web.model.ViewPage;
import com.github.stoynko.easydoc.web.utilities.PageBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static com.github.stoynko.easydoc.web.dto.DtoContext.forFragment;
import static com.github.stoynko.easydoc.web.dto.DtoContext.forPage;
import static com.github.stoynko.easydoc.web.dto.DtoContext.forTargetResource;
import static com.github.stoynko.easydoc.web.model.ViewFragment.DOCTOR_LANDING;
import static com.github.stoynko.easydoc.web.model.ViewFragment.DOCTOR_ONBOARDING;
import static com.github.stoynko.easydoc.web.model.ViewPage.ACCOUNT;
import static com.github.stoynko.easydoc.web.model.ViewPage.DOCTORS;

@Controller
@RequiredArgsConstructor
public class DoctorController {

    private final PageBuilder pageBuilder;
    private final DoctorService doctorService;
    private final PractitionerApplicationService applicationService;

    @GetMapping("/onboarding/doctors")
    public ModelAndView getOnboardingPage(@AuthenticationPrincipal UserAuthenticationDetails principal) {
        ModelAndView modelAndView = pageBuilder.buildPage(forFragment(ViewPage.DOCTOR_ONBOARDING, DOCTOR_LANDING, principal));
        modelAndView.addObject("processState", "landing");
        return modelAndView;
    }

    @GetMapping("/onboarding/doctors/apply")
    @PreAuthorize("@securityCheck.canSubmitPractitionerApplication(principal)")
    public ModelAndView getOnboardingForm(@AuthenticationPrincipal UserAuthenticationDetails principal) {
        return pageBuilder.buildPage(forFragment(ViewPage.DOCTOR_ONBOARDING, DOCTOR_ONBOARDING, principal));
    }

    @PostMapping("/onboarding/doctors/apply")
    @PreAuthorize("@securityCheck.canSubmitPractitionerApplication(principal)")
    public ModelAndView submitOnboardingApplication(@AuthenticationPrincipal UserAuthenticationDetails principal,
                                                    @Valid RegisterPractitionerRequest request,
                                                    BindingResult bindingResult) throws IOException {

        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = pageBuilder.buildPage(forFragment(ViewPage.DOCTOR_ONBOARDING, DOCTOR_ONBOARDING, principal));
            return pageBuilder.addErrors(modelAndView, "registerPractitionerRequest", request, bindingResult);
        }

        applicationService.submitApplication(principal.getId(), request);
        ModelAndView modelAndView = pageBuilder.buildPage(forPage(ViewPage.DOCTOR_ONBOARDING, principal));
        modelAndView.addObject("processState", "success");
        return modelAndView;
    }

    @GetMapping("/doctors")
    public ModelAndView getDoctorsPage(@AuthenticationPrincipal UserAuthenticationDetails principal) {
        return pageBuilder.buildPage(forPage(DOCTORS, principal));
    }

    @GetMapping("/doctors/{doctorId}")
    public ModelAndView getDoctorProfilePage(@AuthenticationPrincipal UserAuthenticationDetails principal,
                                             @PathVariable UUID doctorId) {
        return pageBuilder.buildPage(forTargetResource(ACCOUNT, principal, doctorId));
    }

    @GetMapping(value = "/doctors", params = "category")
    public ModelAndView getDoctorsByCategory(@RequestParam(value = "category", required = false) Expertise expertise,
                                             @AuthenticationPrincipal UserAuthenticationDetails principal) {

        List<DoctorBriefSummaryResponse> doctors = (expertise == null) ?
                doctorService.getAllDoctors() : doctorService.getDoctorsByExpertise(expertise);

        ModelAndView modelAndView = pageBuilder.buildPage(forPage(DOCTORS, principal));
        modelAndView.addObject("doctors", doctors);
        modelAndView.addObject("category", expertise);
        return modelAndView;
    }
}
