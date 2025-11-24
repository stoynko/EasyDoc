package com.github.stoynko.easydoc.web.resolvers;

import com.github.stoynko.easydoc.models.Doctor;
import com.github.stoynko.easydoc.models.User;
import com.github.stoynko.easydoc.models.enums.AccountRole;
import com.github.stoynko.easydoc.services.AppointmentService;
import com.github.stoynko.easydoc.services.DoctorService;
import com.github.stoynko.easydoc.services.PractitionerApplicationService;
import com.github.stoynko.easydoc.services.UserService;
import com.github.stoynko.easydoc.web.dto.DtoAggregator;
import com.github.stoynko.easydoc.web.dto.DtoContext;
import com.github.stoynko.easydoc.web.dto.request.AppointmentRequest;
import com.github.stoynko.easydoc.web.dto.request.LoginRequest;
import com.github.stoynko.easydoc.web.dto.request.RegisterRequest;
import com.github.stoynko.easydoc.web.dto.request.SubmitAccountDetailsRequest;
import com.github.stoynko.easydoc.web.mappers.DtoMapper;
import com.github.stoynko.easydoc.web.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.github.stoynko.easydoc.models.enums.AccountRole.PATIENT;

@Component
public class PatientRoleViewResolver implements RoleViewResolver {

    private final DtoAggregator dtoAggregator;
    private final UserService userService;
    private final DoctorService doctorService;
    private final AppointmentService appointmentService;
    private final PractitionerApplicationService practitionerApplicationService;

    @Autowired
    public PatientRoleViewResolver(DtoAggregator dtoAggregator, UserService userService, DoctorService doctorService, AppointmentService appointmentService, PractitionerApplicationService practitionerApplicationService) {
        this.dtoAggregator = dtoAggregator;
        this.userService = userService;
        this.doctorService = doctorService;
        this.appointmentService = appointmentService;
        this.practitionerApplicationService = practitionerApplicationService;
    }

    @Override
    public AccountRole getSupportedRole() {
        return PATIENT;
    }

    @Override
    public Map<String,Object> buildModelData(DtoContext dtoContext) {

        Map<String, Object> model = new HashMap<>();

        switch (dtoContext.viewPage()) {

            case REGISTER -> model.putIfAbsent("registerRequest", new RegisterRequest());

            case LOGIN -> model.putIfAbsent("loginRequest", new LoginRequest());

            case ACCOUNT_ONBOARDING -> {
                model.put("processState", "formEntry");
                model.putIfAbsent("submitPersonalDetailsRequest", new SubmitAccountDetailsRequest());
            }

            case DASHBOARD -> {}

            case DOCTORS -> model.put("doctors", doctorService.getAllDoctors());

            case PRESCRIPTIONS -> model.put("prescriptions", null);

            case APPOINTMENTS -> {
                model.put("upcomingAppointments", appointmentService.getPatientUpcomingAppointments(dtoContext.principal().getId())
                        .stream().map(DtoMapper::getPatientAppointmentDetailsFrom)
                        .collect(Collectors.toList()));

                model.put("pastAppointments", appointmentService.getPatientPastAppointments(dtoContext.principal().getId())
                        .stream().map(DtoMapper::getPatientAppointmentDetailsFrom)
                        .collect(Collectors.toList()));
            }

            case APPOINTMENT -> {
                model.put("processState", "formEntry");
                Doctor doctor = doctorService.getDoctorByDoctorId(dtoContext.resourceId());
                model.putIfAbsent("doctorSummary", DtoMapper.toDoctorBriefInfoFrom(doctor));

                AppointmentRequest appointmentRequest = new AppointmentRequest();
                appointmentRequest.setDate(LocalDate.now());
                model.putIfAbsent("appointmentRequest", appointmentRequest);
                model.putIfAbsent("timeSlots", appointmentService.getTimeSlotsFor(dtoContext.resourceId(), appointmentRequest.getDate()));
            }

            case ACCOUNT -> {
                Doctor doctor = doctorService.getDoctorByDoctorId(dtoContext.resourceId());
                model.put("doctorSummary", DtoMapper.toDoctorDetailedInfoFrom(doctor));
            }

            case DOCTOR_ONBOARDING -> {
                User user = userService.getUserById(dtoContext.principal().getId());
                model.put("processState", "formEntry");
                model.putIfAbsent("registerPractitionerRequest", DtoMapper.getRegisterPractitionerFrom(user));
            }

            case SETTINGS -> {
                ViewFragment viewFragment = dtoContext.viewFragment();

                if (dtoContext.principal() != null) {
                    dtoAggregator.aggregateDtoForFragment(model, viewFragment, dtoContext.principal().getId());
                }
            }
        }

        if (dtoContext.principal() != null) {
            //model.put("role", dtoContext.principal().getRole());
            model.put("userSummary", DtoMapper.getUserSummary(userService.getUserById(dtoContext.principal().getId())));
            model.put("hasSubmittedApplication", practitionerApplicationService.hasSubmittedApplication(dtoContext.principal().getId()));
        }

        return model;
    }
}