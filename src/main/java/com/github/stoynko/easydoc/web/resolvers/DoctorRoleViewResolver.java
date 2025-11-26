package com.github.stoynko.easydoc.web.resolvers;

import com.github.stoynko.easydoc.exceptions.ReportDoesNotExistException;
import com.github.stoynko.easydoc.models.Appointment;
import com.github.stoynko.easydoc.models.Report;
import com.github.stoynko.easydoc.models.enums.AppointmentStatus;
import com.github.stoynko.easydoc.services.ReportService;
import com.github.stoynko.easydoc.models.Doctor;
import com.github.stoynko.easydoc.models.enums.AccountRole;
import com.github.stoynko.easydoc.services.AppointmentService;
import com.github.stoynko.easydoc.services.DoctorService;
import com.github.stoynko.easydoc.services.UserService;
import com.github.stoynko.easydoc.web.dto.DtoAggregator;
import com.github.stoynko.easydoc.web.dto.DtoContext;
import com.github.stoynko.easydoc.web.dto.request.CreateMedicalReportRequest;
import com.github.stoynko.easydoc.web.dto.request.DiagnosisSearchRequest;
import com.github.stoynko.easydoc.web.mappers.DtoMapper;
import com.github.stoynko.easydoc.web.model.ViewAction;
import com.github.stoynko.easydoc.web.model.ViewFragment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.github.stoynko.easydoc.models.enums.AccountRole.DOCTOR;
import static com.github.stoynko.easydoc.web.model.ViewAction.READ;

@Component
public class DoctorRoleViewResolver implements RoleViewResolver {

    private final DtoAggregator dtoAggregator;
    private final DoctorService doctorService;
    private final UserService userService;
    private final AppointmentService appointmentService;
    private final ReportService reportService;

    @Autowired
    public DoctorRoleViewResolver(DtoAggregator dtoAggregator, DoctorService doctorService, UserService userService, AppointmentService appointmentService, ReportService reportService) {
        this.dtoAggregator = dtoAggregator;
        this.doctorService = doctorService;
        this.userService = userService;
        this.appointmentService = appointmentService;
        this.reportService = reportService;
    }

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

            case APPOINTMENT_REVIEW -> {
                Appointment appointment = appointmentService.getAppointmentById(dtoContext.resourceId());
                model.put("appointmentDetails", DtoMapper.toDoctorAppointmentSummaryResponse(appointment));

                Report medicalReport;

                try {
                    medicalReport = reportService.getById(dtoContext.resourceId());
                } catch (ReportDoesNotExistException exception) {
                    medicalReport = null;
                }

                model.put("reportExists", medicalReport != null);
                model.put("readOnly", dtoContext.action() == READ && medicalReport != null);

                if (medicalReport != null) {
                    model.put("medicalReport", DtoMapper.toMedicalReportRequestFrom(medicalReport));
                } else {
                    model.put("diagnosisSearchRequest", new DiagnosisSearchRequest());
                    model.put("medicalReport", new CreateMedicalReportRequest());
                }

/*                if (appointment.getStatus() == AppointmentStatus.CONFIRMED) {

                } else {
                    model.put("appointmentMedicalReport", null);
                    model.put("appointmentPrescription", null);
                }*/
            }

            case PRESCRIPTIONS -> {

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

