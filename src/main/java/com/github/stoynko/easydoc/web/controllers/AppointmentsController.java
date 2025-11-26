package com.github.stoynko.easydoc.web.controllers;

import com.github.stoynko.easydoc.client.Icd10Client;
import com.github.stoynko.easydoc.models.Appointment;
import com.github.stoynko.easydoc.security.UserAuthenticationDetails;
import com.github.stoynko.easydoc.services.AppointmentService;
import com.github.stoynko.easydoc.services.ReportService;
import com.github.stoynko.easydoc.web.dto.request.AppointmentRequest;
import com.github.stoynko.easydoc.web.dto.request.CreateMedicalReportRequest;
import com.github.stoynko.easydoc.web.dto.response.AppointmentTimeSlotResponse;
import com.github.stoynko.easydoc.web.dto.response.Icd10SearchResult;
import com.github.stoynko.easydoc.web.model.ViewAction;
import com.github.stoynko.easydoc.web.utilities.PageBuilder;
import jakarta.validation.Valid;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

import static com.github.stoynko.easydoc.models.enums.AccountRole.PATIENT;
import static com.github.stoynko.easydoc.web.dto.DtoContext.forPage;
import static com.github.stoynko.easydoc.web.dto.DtoContext.forTargetResource;
import static com.github.stoynko.easydoc.web.dto.DtoContext.forTargetResourceWithAction;
import static com.github.stoynko.easydoc.web.model.ViewAction.READ;
import static com.github.stoynko.easydoc.web.model.ViewPage.APPOINTMENTS_TABLE;
import static com.github.stoynko.easydoc.web.model.ViewPage.APPOINTMENT_CREATION;
import static com.github.stoynko.easydoc.web.model.ViewPage.APPOINTMENT_REVIEW;
import static com.github.stoynko.easydoc.web.model.ViewPage.CONFIRMATION;


@Controller
@RequiredArgsConstructor
public class AppointmentsController {

    private final PageBuilder pageBuilder;
    private final AppointmentService appointmentService;
    private final ReportService reportService;
    private final Icd10Client icd10Client;

    @GetMapping("/appointments")
    public ModelAndView getAppointmentsPage(@AuthenticationPrincipal UserAuthenticationDetails principal) {
        return pageBuilder.buildPage(forPage(APPOINTMENTS_TABLE, principal));
    }

    @GetMapping("/appointments/doctor/{doctorId}")
    @PreAuthorize(value = "@securityCheck.canBookAppointment(principal)")
    public ModelAndView getAppointmentPage(@AuthenticationPrincipal UserAuthenticationDetails principal,
                                           @PathVariable UUID doctorId) {

        ModelAndView modelAndView = pageBuilder.buildPage(forTargetResource(APPOINTMENT_CREATION, principal, doctorId));
        modelAndView.addObject("processState", "formEntry");
        return modelAndView;
    }

    @PostMapping("/appointments/doctor/{doctorId}")
    @PreAuthorize(value = "@securityCheck.canBookAppointment(principal)")
    public ModelAndView submitAppointmentPage(@AuthenticationPrincipal UserAuthenticationDetails principal,
                                              @PathVariable UUID doctorId,
                                              @Valid AppointmentRequest request,
                                              BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = pageBuilder.buildPage(
                    forTargetResource(APPOINTMENT_CREATION, principal, doctorId)
            );
            modelAndView.addObject("processState", "formEntry");
            return pageBuilder.addErrors(modelAndView, "appointmentRequest", request, bindingResult);
        }

        appointmentService.createAppointment(principal.getId(), doctorId, request);
        ModelAndView modelAndView = pageBuilder.buildPage(forTargetResource(APPOINTMENT_CREATION, principal, doctorId));
        modelAndView.addObject("processState", "success");
        return modelAndView;
    }

    @GetMapping("/appointments/doctor/{doctorId}/available-times")
    @PreAuthorize(value = "@securityCheck.canBookAppointment(principal)")
    public String getAvailableSlots(@PathVariable UUID doctorId,
                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                    Model model) {

        List<AppointmentTimeSlotResponse> slots = appointmentService.getTimeSlotsFor(doctorId, date);
        model.addAttribute("timeSlots", slots);
        return "fragments/components/time_slots :: time-slots";
    }

    @PostMapping("/appointments/{appointmentId}/cancel")
    public ModelAndView cancelAppointment(@AuthenticationPrincipal UserAuthenticationDetails principal,
                                         @PathVariable UUID appointmentId) {

        appointmentService.cancelAppointment(appointmentId, principal);
        ModelAndView modelAndView = pageBuilder.buildPage(forPage(CONFIRMATION, principal));
        return modelAndView.addObject("confirmationMessage", "appointmentCancellationSuccess");
    }

    @PostMapping("/appointments/{appointmentId}/no-show")
    public ModelAndView markAppointmentAsNoShow(@AuthenticationPrincipal UserAuthenticationDetails principal,
                                          @PathVariable UUID appointmentId) {

        appointmentService.markAsNoShow(appointmentId, principal);
        ModelAndView modelAndView = pageBuilder.buildPage(forPage(CONFIRMATION, principal));
        return modelAndView.addObject("confirmationMessage", "appointmentMarkNoShow");
    }

    @PreAuthorize("hasAnyRole('PATIENT', 'DOCTOR')")
    @GetMapping("/appointments/{appointmentId}/review")
    public ModelAndView viewAppointmentPage(@AuthenticationPrincipal UserAuthenticationDetails principal,
                                            @PathVariable UUID appointmentId,
                                            @RequestParam(name = "action", required = false, defaultValue = "READ") ViewAction action) {
        if (principal.getRole() == PATIENT) {
            action = READ;
        }
        ModelAndView modelAndView = pageBuilder.buildPage(forTargetResourceWithAction(APPOINTMENT_REVIEW, principal, appointmentId, action));
        return modelAndView;
    }

    @PreAuthorize("hasRole('DOCTOR')")
    @GetMapping("search/diagnosis")
    public String searchDiagnosis(@RequestParam("keyword") String keyword, Model model) {



        System.out.println(keyword);

        Icd10SearchResult result = icd10Client.search(keyword, 100);
        return null;
    }

    @PreAuthorize("hasRole('DOCTOR')")
    @PostMapping("/appointments/{appointmentId}/report/submit")
    public ModelAndView createMedicalReport(@AuthenticationPrincipal UserAuthenticationDetails principal,
                                            @PathVariable UUID appointmentId,
                                            @Valid CreateMedicalReportRequest request,
                                            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = pageBuilder.buildPage(forTargetResource(APPOINTMENT_REVIEW, principal, appointmentId));
            ModelAndView modelAndView1 = pageBuilder.addErrors(modelAndView, "medicalReport", request, bindingResult);
            return modelAndView1;
        }

        Appointment appointment = appointmentService.getAppointmentById(appointmentId);
        boolean medicalReportExists = appointment.hasReport();

        if (medicalReportExists) {
            reportService.editMedicalReport(appointmentId, request);
        } else {
            reportService.createMedicalReport(appointmentId, request);
        }

        ModelAndView modelAndView = pageBuilder.buildPage(forPage(CONFIRMATION, principal));
        modelAndView.addObject("appointmentId", appointmentId);
        return modelAndView.addObject("confirmationMessage", "reportCreationSuccess");
    }
}
















/*@PostMapping("/appointments/{appointmentId}/report")
public ModelAndView getReportPage(@AuthenticationPrincipal UserAuthenticationDetails principal,
                                  @PathVariable UUID appointmentId) {

    ModelAndView modelAndView = pageBuilder.buildPage(forTargetResource(MEDICAL_REPORT_VIEW, principal, appointmentId));
    return modelAndView;
}*/

/*
    @PostMapping("/appointments/{appointmentId}/view")
    public ModelAndView viewAppointmentDetails(@AuthenticationPrincipal UserAuthenticationDetails principal,
                                                @PathVariable UUID appointmentId) {

        appointmentService.markAsNoShow(appointmentId, principal);
        ModelAndView modelAndView = pageBuilder.buildPage(forPage(CONFIRMATION, principal));
        return modelAndView.addObject("confirmationMessage", "appointmentMarkNoShow");
    }*/

    /*@PreAuthorize("hasRole('DOCTOR')")
    @GetMapping("/appointments/{appointmentId}/report/submit")
    public ModelAndView getMedicalReportCreationPage(@AuthenticationPrincipal UserAuthenticationDetails principal,
                                              @PathVariable UUID appointmentId) {

        ModelAndView modelAndView = pageBuilder.buildPage(forTargetResource(MEDICAL_REPORT_CREATION, principal, appointmentId));
        return modelAndView;
    }*/

