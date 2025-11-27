package com.github.stoynko.easydoc.web.resolvers;

import com.github.stoynko.easydoc.models.Appointment;
import com.github.stoynko.easydoc.models.Doctor;
import com.github.stoynko.easydoc.models.enums.AccountRole;
import com.github.stoynko.easydoc.services.AppointmentService;
import com.github.stoynko.easydoc.services.DoctorService;
import com.github.stoynko.easydoc.web.dto.DtoAggregator;
import com.github.stoynko.easydoc.web.dto.DtoContext;
import com.github.stoynko.easydoc.web.dto.request.MedicalReportRequest;
import com.github.stoynko.easydoc.web.mappers.DtoMapper;
import com.github.stoynko.easydoc.web.model.ViewFragment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.github.stoynko.easydoc.models.enums.AccountRole.DOCTOR;
import static com.github.stoynko.easydoc.web.model.ViewAction.READ;
import static com.github.stoynko.easydoc.web.model.ViewAction.WRITE;

@Component
@RequiredArgsConstructor
public class DoctorRoleViewResolver implements RoleViewResolver {

    private final DtoAggregator dtoAggregator;
    private final DoctorService doctorService;
    private final AppointmentService appointmentService;

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
                        .stream().map(DtoMapper::toDoctorAppointmentDetailsFrom)
                        .collect(Collectors.toList()));

                model.put("pastAppointments", appointmentService.getDoctorPastAppointments(dtoContext.principal().getId())
                        .stream().map(DtoMapper::toDoctorAppointmentDetailsFrom)
                        .collect(Collectors.toList()));
            }

            case MEDICAL_REPORT_VIEW -> {
                Appointment appointment = appointmentService.getAppointmentById(dtoContext.resourceId());
                model.put("appointmentDetails", DtoMapper.toDoctorAppointmentSummaryResponse(appointment));
                model.put("reportExists", appointment.hasReport());
                model.put("action", (appointment.hasReport() && dtoContext.action() == READ) ? READ : WRITE);

                if (appointment.hasReport()) {
                    model.put("medicalReport", DtoMapper.toMedicalReportResponseFrom(appointment.getReport()));
                } else {
                    model.put("medicalReport", new MedicalReportRequest());
                }

               /* if (appointment.hasReport() && dtoContext.action() == READ) {
                    model.put("medicalReport", DtoMapper.toMedicalReportResponseFrom(appointment.getReport()));
                } else if (appointment.hasReport() && dtoContext.action() == WRITE) {
                    model.put("medicalReport", DtoMapper.toMedicalReportResponseFrom(appointment.getReport()));
                } else {
                    model.put("medicalReport", new MedicalReportRequest());
                }*/
            }

            case PRESCRIPTION_VIEW -> {
                Appointment appointment = appointmentService.getAppointmentById(dtoContext.resourceId());
                model.put("appointmentDetails", DtoMapper.toDoctorAppointmentSummaryResponse(appointment));

                boolean prescriptionExists = false;

                model.put("prescriptionExists", !prescriptionExists);
                model.put("readOnly", dtoContext.action() == READ && prescriptionExists);

                if (prescriptionExists) {
                    model.put("prescription", null);
                } else {
                    model.put("prescription", null);
                }
            }

            case PRESCRIPTIONS_TABLE -> {

                model.put("prescriptions", null);
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
            Doctor doctor = doctorService.getDoctorDetailsByUserId(dtoContext.principal().getId());
            model.put("doctorSummary", DtoMapper.toDoctorDetailedInfoFrom(doctor));
            model.put("userSummary", DtoMapper.toDoctorDetailedInfoFrom(doctor));
        }

        return model;
    }
}

