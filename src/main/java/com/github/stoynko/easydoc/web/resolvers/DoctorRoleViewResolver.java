package com.github.stoynko.easydoc.web.resolvers;

import com.github.stoynko.easydoc.appointment.model.Appointment;
import com.github.stoynko.easydoc.appointment.service.AppointmentService;
import com.github.stoynko.easydoc.appointment.web.mapper.AppointmentMapper;
import com.github.stoynko.easydoc.practitioner.model.Doctor;
import com.github.stoynko.easydoc.practitioner.service.DoctorService;
import com.github.stoynko.easydoc.prescription.web.dto.request.AddMedicamentRequest;
import com.github.stoynko.easydoc.prescription.web.dto.request.RemoveMedicamentRequest;
import com.github.stoynko.easydoc.report.service.ReportService;
import com.github.stoynko.easydoc.report.web.dto.request.MedicalReportRequest;
import com.github.stoynko.easydoc.user.model.AccountRole;
import com.github.stoynko.easydoc.user.model.User;
import com.github.stoynko.easydoc.user.service.UserService;
import com.github.stoynko.easydoc.web.dto.DtoAggregator;
import com.github.stoynko.easydoc.web.dto.DtoContext;
import com.github.stoynko.easydoc.web.model.ViewFragment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.github.stoynko.easydoc.appointment.web.mapper.AppointmentMapper.toDoctorAppointmentSummaryResponse;
import static com.github.stoynko.easydoc.practitioner.web.mapper.PractitionerMapper.toDoctorDetailedInfoFrom;
import static com.github.stoynko.easydoc.report.model.ReportStatus.DRAFT;
import static com.github.stoynko.easydoc.report.web.mapper.ReportMapper.toMedicalReportResponseFrom;
import static com.github.stoynko.easydoc.user.model.AccountRole.DOCTOR;
import static com.github.stoynko.easydoc.web.utilities.ValidationUtilities.getReportActionFor;

@Component
@RequiredArgsConstructor
public class DoctorRoleViewResolver implements RoleViewResolver {

    private final DtoAggregator dtoAggregator;
    private final UserService userService;
    private final DoctorService doctorService;
    private final AppointmentService appointmentService;
    private final ReportService reportService;

    @Override
    public AccountRole getSupportedRole() {
        return DOCTOR;
    }

    @Override
    public Map<String, Object> buildModelData(DtoContext dtoContext) {

        Map<String, Object> model = new HashMap<>();

        switch (dtoContext.viewPage()) {

            case DASHBOARD -> {

                model.put("profileSummary", null);
            }

            case APPOINTMENTS_TABLE -> {
                model.put("upcomingAppointments", appointmentService.getDoctorUpcomingAppointments(dtoContext.principal().getId())
                        .stream().map(AppointmentMapper::toDoctorAppointmentDetailsFrom)
                        .collect(Collectors.toList()));

                model.put("pastAppointments", appointmentService.getDoctorPastAppointments(dtoContext.principal().getId())
                        .stream().map(AppointmentMapper::toDoctorAppointmentDetailsFrom)
                        .collect(Collectors.toList()));
            }

            case PRESCRIPTIONS_TABLE -> {

                model.put("prescriptions", null);
            }

            case MEDICAL_REPORT_VIEW -> {
                Appointment appointment = appointmentService.getAppointmentById(dtoContext.resourceId());

                model.put("action", getReportActionFor(appointment));
                model.put("reportExists", appointment.hasReport());
                model.put("appointmentDetails", toDoctorAppointmentSummaryResponse(appointment));

                if (!appointment.hasReport()) {
                    model.put("medicalReport", new MedicalReportRequest());
                    model.put("documentStatus", DRAFT);
                } else {
                    model.put("medicalReport", toMedicalReportResponseFrom(appointment.getReport()));
                    model.put("documentStatus", appointment.getReport().getReportStatus());
                }
            }

            case PRESCRIPTION_VIEW -> {
                Appointment appointment = appointmentService.getAppointmentById(dtoContext.resourceId());

                model.put("appointmentDetails", toDoctorAppointmentSummaryResponse(appointment));
                model.put("prescriptionExists", dtoContext.content() != null);

                if (dtoContext.content() != null) {
                    model.put("prescriptionDetails", dtoContext.content());
                    model.put("addMedicamentRequest", new AddMedicamentRequest());
                    model.put("removeMedicamentRequest", new RemoveMedicamentRequest());
                }
            }

            case SETTINGS -> {
                ViewFragment viewFragment = dtoContext.viewFragment();

                if (dtoContext.principal() != null) {
                    dtoAggregator.aggregateDtoForFragment(model, viewFragment, dtoContext.principal().getId());
                } else {
                    dtoAggregator.aggregateDtoForFragment(model, viewFragment, null);
                }
            }
        }

        if (dtoContext.principal() != null) {

            User user = userService.getUserById(dtoContext.principal().getId());
            Doctor doctor = doctorService.getDoctorDetailsByUserId(user.getId());

            model.put("isProfileCompleted", user.isProfileCompleted());
            model.put("doctorSummary", toDoctorDetailedInfoFrom(doctor));
            model.put("userSummary", toDoctorDetailedInfoFrom(doctor));
        }

        return model;
    }
}

