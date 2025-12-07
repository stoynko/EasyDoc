package com.github.stoynko.easydoc.appointment.web;

import com.github.stoynko.easydoc.appointment.model.Appointment;
import com.github.stoynko.easydoc.appointment.model.AppointmentStatus;
import com.github.stoynko.easydoc.prescription.model.DeliveryMethod;
import com.github.stoynko.easydoc.prescription.service.MedicamentsCatalog;
import com.github.stoynko.easydoc.prescription.web.dto.response.MedicamentItemResponse;
import com.github.stoynko.easydoc.prescription.web.dto.response.PrescriptionResponse;
import com.github.stoynko.easydoc.report.model.ReportStatus;
import com.github.stoynko.easydoc.security.UserAuthenticationDetails;
import com.github.stoynko.easydoc.appointment.service.AppointmentService;
import com.github.stoynko.easydoc.prescription.service.PrescriptionService;
import com.github.stoynko.easydoc.report.service.ReportService;
import com.github.stoynko.easydoc.appointment.web.dto.request.AppointmentRequest;
import com.github.stoynko.easydoc.report.web.dto.request.MedicalReportRequest;
import com.github.stoynko.easydoc.appointment.web.dto.response.AppointmentTimeSlotResponse;
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

import static com.github.stoynko.easydoc.appointment.model.AppointmentStatus.PENDING;
import static com.github.stoynko.easydoc.appointment.model.AppointmentStatus.PROCESSING;
import static com.github.stoynko.easydoc.report.model.ReportStatus.DRAFT;
import static com.github.stoynko.easydoc.web.dto.DtoContext.forPage;
import static com.github.stoynko.easydoc.web.dto.DtoContext.forTargetResource;
import static com.github.stoynko.easydoc.web.dto.DtoContext.forTargetResourceWithAction;
import static com.github.stoynko.easydoc.web.dto.DtoContext.forTargetResourceWithContent;
import static com.github.stoynko.easydoc.web.model.ViewAction.READ;
import static com.github.stoynko.easydoc.web.model.ViewAction.WRITE;
import static com.github.stoynko.easydoc.web.model.ViewPage.APPOINTMENTS_TABLE;
import static com.github.stoynko.easydoc.web.model.ViewPage.APPOINTMENT_CREATION;
import static com.github.stoynko.easydoc.web.model.ViewPage.CONFIRMATION;
import static com.github.stoynko.easydoc.web.model.ViewPage.MEDICAL_REPORT_VIEW;
import static com.github.stoynko.easydoc.web.model.ViewPage.PRESCRIPTION_VIEW;

@Controller
@RequiredArgsConstructor
public class AppointmentsController {

    private final PageBuilder pageBuilder;
    private final AppointmentService appointmentService;
    private final ReportService reportService;
    private final PrescriptionService prescriptionService;
    private final MedicamentsCatalog medicamentsCatalog;

    @GetMapping("/appointments")
    public ModelAndView getAppointmentsPage(@AuthenticationPrincipal UserAuthenticationDetails principal) {
        return pageBuilder.buildPage(forPage(APPOINTMENTS_TABLE, principal));
    }

    @GetMapping("/appointments/doctors/{doctorId}")
    @PreAuthorize(value = "@securityCheck.canBookAppointment(principal)")
    public ModelAndView getAppointmentPage(@AuthenticationPrincipal UserAuthenticationDetails principal,
                                           @PathVariable UUID doctorId) {

        ModelAndView modelAndView = pageBuilder.buildPage(forTargetResource(APPOINTMENT_CREATION, principal, doctorId));
        modelAndView.addObject("processState", "formEntry");
        return modelAndView;
    }

    @PostMapping("/appointments/doctors/{doctorId}")
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

    @GetMapping("/appointments/doctors/{doctorId}/available-times")
    @PreAuthorize(value = "@securityCheck.canBookAppointment(principal)")
    public String getAvailableSlots(@PathVariable UUID doctorId,
                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                    Model model) {

        List<AppointmentTimeSlotResponse> slots = appointmentService.getTimeSlotsFor(doctorId, date);
        model.addAttribute("timeSlots", slots);
        return "fragments/time_slots :: time-slots";
    }

    @PostMapping("/appointments/{appointmentId}/conclude")
    @PreAuthorize("hasRole('DOCTOR')")
    public ModelAndView concludeAppointment(@PathVariable UUID appointmentId) {
        appointmentService.concludeAppointment(appointmentId);
        return new ModelAndView("redirect:/appointments");
    }

    @PreAuthorize("hasRole('DOCTOR')")
    @PostMapping("/appointments/{appointmentId}/process")
    public ModelAndView ProcessAppointment(@AuthenticationPrincipal UserAuthenticationDetails principal,
                                          @PathVariable UUID appointmentId) {

        appointmentService.processAppointment(appointmentId, principal);
        return new ModelAndView("redirect:/appointments");
    }

    @PreAuthorize("hasRole('PATIENT')")
    @PostMapping("/appointments/{appointmentId}/cancel")
    public ModelAndView cancelAppointment(@AuthenticationPrincipal UserAuthenticationDetails principal,
                                         @PathVariable UUID appointmentId) {

        Appointment appointment = appointmentService.getAppointmentById(appointmentId);
        if (appointment.getStatus() != PENDING) {
            return new ModelAndView("redirect:/appointments");
        }

        appointmentService.cancelAppointment(appointmentId, principal);
        ModelAndView modelAndView = pageBuilder.buildPage(forPage(CONFIRMATION, principal));
        return modelAndView.addObject("confirmationMessage", "appointmentCancellationSuccess");
    }

    @PreAuthorize("hasRole('DOCTOR')")
    @PostMapping("/appointments/{appointmentId}/no-show")
    public ModelAndView markAppointmentAsNoShow(@AuthenticationPrincipal UserAuthenticationDetails principal,
                                          @PathVariable UUID appointmentId) {

        Appointment appointment = appointmentService.getAppointmentById(appointmentId);
        if (appointment.getStatus() == PENDING) {
            return new ModelAndView("redirect:/appointments");
        }

        appointmentService.markAsNoShow(appointmentId, principal);
        ModelAndView modelAndView = pageBuilder.buildPage(forPage(CONFIRMATION, principal));
        return modelAndView.addObject("confirmationMessage", "appointmentMarkNoShow");
    }

    @PreAuthorize("hasAnyRole('PATIENT', 'DOCTOR')")
    @GetMapping("/appointments/{appointmentId}/report")
    public ModelAndView viewReportPage(@AuthenticationPrincipal UserAuthenticationDetails principal,
                                            @PathVariable UUID appointmentId) {
        return pageBuilder.buildPage(forTargetResource(MEDICAL_REPORT_VIEW, principal, appointmentId));
    }

    @PreAuthorize("hasRole('DOCTOR')")
    @GetMapping("/appointments/{appointmentId}/report/edit")
    public ModelAndView editReportPage(@AuthenticationPrincipal UserAuthenticationDetails principal,
                                       @PathVariable UUID appointmentId) {

        Appointment appointment = appointmentService.getAppointmentById(appointmentId);
        if (appointment.getReport() != null && appointment.getReport().getReportStatus() != DRAFT) {
            return new ModelAndView("redirect:/appointments/" + appointmentId + "/report");
        }

        return pageBuilder.buildPage(forTargetResource(MEDICAL_REPORT_VIEW, principal, appointmentId));
    }

    @PreAuthorize("hasRole('DOCTOR')")
    @PostMapping("/appointments/{appointmentId}/report/submit")
    public ModelAndView createMedicalReport(@AuthenticationPrincipal UserAuthenticationDetails principal,
                                            @PathVariable UUID appointmentId,
                                            @Valid MedicalReportRequest request,
                                            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = pageBuilder.buildPage(forTargetResource(MEDICAL_REPORT_VIEW, principal, appointmentId));
            pageBuilder.addErrors(modelAndView, "medicalReport", request, bindingResult);
            return modelAndView;
        }

        Appointment appointment = appointmentService.getAppointmentById(appointmentId);
        boolean medicalReportExists = appointment.hasReport();

        ModelAndView modelAndView = pageBuilder.buildPage(forPage(CONFIRMATION, principal));

        if (medicalReportExists) {
            reportService.editMedicalReport(appointment.getReport(), request);
            modelAndView.addObject("confirmationMessage", "reportEditSuccess");
        } else {
            reportService.createMedicalReport(appointment, request);
            modelAndView.addObject("confirmationMessage", "reportCreationSuccess");
        }

        return modelAndView;
    }

    @PreAuthorize("hasRole('DOCTOR')")
    @PostMapping("/appointments/{appointmentId}/report/issue")
    public ModelAndView issueReport(@AuthenticationPrincipal UserAuthenticationDetails principal,
                                       @PathVariable UUID appointmentId) {

        Appointment appointment = appointmentService.getAppointmentById(appointmentId);

        reportService.issueMedicalReport(appointment.getReport());
        return pageBuilder.buildPage(forTargetResource(MEDICAL_REPORT_VIEW, principal, appointmentId));
    }

    @PreAuthorize("hasAnyRole('PATIENT', 'DOCTOR')")
    @GetMapping("/appointments/{appointmentId}/prescription")
    public ModelAndView viewAppointmentPrescriptionPage(@AuthenticationPrincipal UserAuthenticationDetails principal,
                                            @PathVariable UUID appointmentId) {

        PrescriptionResponse prescription = prescriptionService.getPrescription(appointmentId);

        ModelAndView modelAndView = pageBuilder.buildPage(forTargetResourceWithContent(PRESCRIPTION_VIEW, principal, appointmentId, prescription));

        if (prescription != null) {
            List<MedicamentItemResponse> medicaments = medicamentsCatalog.getMedicamentsCatalog();
            modelAndView.addObject("medicamentsCatalog", medicaments);
            modelAndView.addObject("methods", DeliveryMethod.values());
        }
        return modelAndView;
    }

    @PostMapping("/appointments/{appointmentId}/prescription/create")
    public ModelAndView createPrescription(@PathVariable UUID appointmentId) {

        prescriptionService.createPrescription(appointmentId);
        return new ModelAndView("redirect:/appointments/" + appointmentId + "/prescription");
    }

    @PreAuthorize("hasRole('DOCTOR')")
    @GetMapping("/search/diagnosis")
    public String searchDiagnosis(@RequestParam("keyword") String keyword, Model model) {
        model.addAttribute("diagnosisList", reportService.findDiagnosisByKeyword(keyword));
        return "fragments/diagnosis_selector :: diagnosis_selector";
    }
}


