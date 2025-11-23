package com.github.stoynko.easydoc.web.controllers;

import com.github.stoynko.easydoc.models.enums.Expertise;
import com.github.stoynko.easydoc.security.UserAuthenticationDetails;
import com.github.stoynko.easydoc.services.AppointmentService;
import com.github.stoynko.easydoc.services.DoctorService;
import com.github.stoynko.easydoc.web.dto.request.AppointmentRequest;
import com.github.stoynko.easydoc.web.dto.response.AppointmentTimeSlotResponse;
import com.github.stoynko.easydoc.web.dto.response.DoctorBriefSummaryResponse;
import com.github.stoynko.easydoc.web.resolvers.RoleViewResolverRegistry;
import com.github.stoynko.easydoc.web.utilities.PageBuilder;
import jakarta.validation.Valid;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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

import static com.github.stoynko.easydoc.web.dto.DtoContext.forPage;
import static com.github.stoynko.easydoc.web.dto.DtoContext.forTargetResource;
import static com.github.stoynko.easydoc.web.model.ViewPage.ACCOUNT;
import static com.github.stoynko.easydoc.web.model.ViewPage.APPOINTMENT;
import static com.github.stoynko.easydoc.web.model.ViewPage.APPOINTMENTS;
import static com.github.stoynko.easydoc.web.model.ViewFragment.NONE;
import static com.github.stoynko.easydoc.web.model.ViewPage.CONFIRMATION;
import static com.github.stoynko.easydoc.web.model.ViewPage.DOCTORS;

@Controller
public class AppointmentsController {

    private final RoleViewResolverRegistry viewResolverRegistry;
    private final PageBuilder pageBuilder;
    private final DoctorService doctorService;
    private final AppointmentService appointmentService;

    @Autowired
    public AppointmentsController(RoleViewResolverRegistry viewResolverRegistry, PageBuilder pageBuilder, DoctorService doctorService, AppointmentService appointmentService, AppointmentService appointmentService1) {
        this.viewResolverRegistry = viewResolverRegistry;
        this.pageBuilder = pageBuilder;
        this.doctorService = doctorService;
        this.appointmentService = appointmentService1;
    }

    @GetMapping("/appointments")
    public ModelAndView getAppointmentsPage(@AuthenticationPrincipal UserAuthenticationDetails principal) {
        return pageBuilder.buildPage(forPage(APPOINTMENTS, principal));
    }

    @GetMapping("/appointments/doctor/{doctorId}")
    public ModelAndView getAppointmentPage(@AuthenticationPrincipal UserAuthenticationDetails principal,
                                  @PathVariable UUID doctorId) {

        ModelAndView modelAndView = pageBuilder.buildPage(forTargetResource(APPOINTMENT, principal, doctorId));
        modelAndView.addObject("processState", "formEntry");
        return modelAndView;
    }

    @GetMapping("/appointments/doctor/{doctorId}/available-times")
    public String getAvailableSlots(@PathVariable UUID doctorId,
                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                    Model model) {

        List<AppointmentTimeSlotResponse> slots = appointmentService.getTimeSlotsFor(doctorId, date);

        model.addAttribute("timeSlots", slots);

        return "fragments/components/time_slots :: time-slots";
    }



    @PostMapping("/appointments/doctor/{doctorId}")
    public ModelAndView submitAppointmentPage(@AuthenticationPrincipal UserAuthenticationDetails principal,
                                              @PathVariable UUID doctorId,
                                              @Valid AppointmentRequest request,
                                              BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = pageBuilder.buildPage(
                    forTargetResource(APPOINTMENT, principal, doctorId)
            );
            modelAndView.addObject("processState", "formEntry");
            return pageBuilder.addErrors(modelAndView, "appointmentRequest", request, bindingResult);
        }

        appointmentService.createAppointment(principal.getId(), doctorId, request);
        ModelAndView modelAndView = pageBuilder.buildPage(forTargetResource(APPOINTMENT, principal, doctorId));
        modelAndView.addObject("processState", "success");
        return modelAndView;
    }

    @GetMapping("/doctor/{doctorId}")
    public ModelAndView getDoctorProfilePage(@AuthenticationPrincipal UserAuthenticationDetails principal,
                                                @PathVariable UUID doctorId) {

        return pageBuilder.buildPage(forTargetResource(ACCOUNT, principal, doctorId));

        //Doctor doctor = doctorService.getDoctorById(uuid);
        /*modelAndView.addObject("doctorSummary", DoctorMapper.toDoctorDetailedInfoFrom(doctor));
        return modelAndView;*/
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

    @PostMapping("/appointments/{appointmentId}/cancel")
    public ModelAndView cancelAppointment(@AuthenticationPrincipal UserAuthenticationDetails principal,
                                         @PathVariable UUID appointmentId) {

        appointmentService.cancelAppointment(appointmentId, principal);
        ModelAndView modelAndView = pageBuilder.buildPage(forPage(CONFIRMATION, principal));
        return modelAndView.addObject("confirmationMessage", "appointmentCancellationSuccess");
    }
}