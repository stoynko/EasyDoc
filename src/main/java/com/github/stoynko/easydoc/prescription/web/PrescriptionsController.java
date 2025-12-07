package com.github.stoynko.easydoc.prescription.web;

import com.github.stoynko.easydoc.appointment.model.Appointment;
import com.github.stoynko.easydoc.appointment.service.AppointmentService;
import com.github.stoynko.easydoc.prescription.model.MedicamentType;
import com.github.stoynko.easydoc.prescription.service.MedicamentsCatalog;
import com.github.stoynko.easydoc.prescription.service.PrescriptionService;
import com.github.stoynko.easydoc.prescription.web.dto.request.AddMedicamentRequest;
import com.github.stoynko.easydoc.prescription.web.dto.response.MedicamentItemResponse;
import com.github.stoynko.easydoc.prescription.web.dto.response.PrescriptionResponse;
import com.github.stoynko.easydoc.security.UserAuthenticationDetails;
import com.github.stoynko.easydoc.web.dto.DtoContext;
import com.github.stoynko.easydoc.web.utilities.PageBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
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

import static com.github.stoynko.easydoc.web.dto.DtoContext.forTargetResourceWithContent;
import static com.github.stoynko.easydoc.web.model.ViewPage.PRESCRIPTIONS_TABLE;
import static com.github.stoynko.easydoc.web.model.ViewPage.PRESCRIPTION_VIEW;

@Controller
@RequiredArgsConstructor
public class PrescriptionsController {

    private final PageBuilder pageBuilder;
    private final MedicamentsCatalog medicamentsCatalog;
    private final PrescriptionService prescriptionService;
    private final AppointmentService appointmentService;

    @GetMapping("/prescriptions")
    @PreAuthorize("hasAnyRole('PATIENT', 'DOCTOR')")
    public ModelAndView getPrescriptionsPage(@AuthenticationPrincipal UserAuthenticationDetails principal) {
        return pageBuilder.buildPage(DtoContext.forPage(PRESCRIPTIONS_TABLE, principal));
    }

    @PostMapping("/prescriptions/{prescriptionId}/medicaments/add")
    @PreAuthorize("hasRole('DOCTOR')")
    public ModelAndView addMedicament(@AuthenticationPrincipal UserAuthenticationDetails principal,
                                      @RequestParam UUID appointmentId,
                                      @Valid AddMedicamentRequest request,
                                      BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            PrescriptionResponse prescription = prescriptionService.getPrescription(appointmentId);
            ModelAndView modelAndView = pageBuilder.buildPage(forTargetResourceWithContent
                    (PRESCRIPTION_VIEW, principal, appointmentId, prescription));

            modelAndView.addObject("addMedicamentRequest", request);
            pageBuilder.addErrors(modelAndView, "addMedicamentRequest", request, bindingResult);
            return modelAndView;
        }

        prescriptionService.addMedicament(appointmentId, request);
        return new ModelAndView("redirect:/appointments/" + appointmentId + "/prescription");
    }

    @PostMapping("/appointments/{appointmentId}/prescription/medicaments/remove/{medicamentId}")
    public ModelAndView removeMedicament(@PathVariable UUID appointmentId, @PathVariable UUID medicamentId) {

        Appointment appointment = appointmentService.getAppointmentById(appointmentId);
        prescriptionService.removeMedicament(appointmentId, appointment.getPrescription(), medicamentId);
        return new ModelAndView("redirect:/appointments/" + appointmentId + "/prescription");
    }

    @PostMapping("appointments/{appointmentId}/prescription/issue")
    public ModelAndView issuePrescription(@PathVariable UUID appointmentId) {
        Appointment appointment = appointmentService.getAppointmentById(appointmentId);
        prescriptionService.issuePrescription(appointmentId, appointment.getPrescription());
        return new ModelAndView("redirect:/appointments/" + appointmentId + "/prescription");
    }
    @PreAuthorize("hasAnyRole('DOCTOR')")
    @GetMapping("/prescriptions/medicaments/options")
    public String getMedicamentsByType(@RequestParam("medicamentType") MedicamentType medicamentType, Model model) {

        List<MedicamentItemResponse> filteredMedicaments = medicamentsCatalog.getFilteredMedicamentsCatalog(medicamentType);
        model.addAttribute("medicamentsCatalog", filteredMedicaments);
        model.addAttribute("methods", medicamentsCatalog.getFilteredByDeliveryMethod(filteredMedicaments));

        return "fragments/medicament_selector :: medicament_block";
    }

    @PreAuthorize("hasAnyRole('DOCTOR')")
    @GetMapping("/prescriptions/medicaments/delivery-methods")
    public String getDeliveryMethodsForMedicament(@RequestParam("medicamentId") UUID medicamentId,
                                                  Model model) {

        model.addAttribute("methods", medicamentsCatalog.getDeliveryMethods(medicamentId));
        return "fragments/medicament_delivery :: delivery_method_fragment";
    }
}
