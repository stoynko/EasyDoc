package com.github.stoynko.easydoc.web.resolvers;

import com.github.stoynko.easydoc.appointment.web.mapper.AppointmentMapper;
import com.github.stoynko.easydoc.practitioner.model.Doctor;
import com.github.stoynko.easydoc.user.model.User;
import com.github.stoynko.easydoc.user.model.AccountRole;
import com.github.stoynko.easydoc.appointment.service.AppointmentService;
import com.github.stoynko.easydoc.practitioner.service.DoctorService;
import com.github.stoynko.easydoc.practitioner.service.PractitionerApplicationService;
import com.github.stoynko.easydoc.user.service.UserService;
import com.github.stoynko.easydoc.web.dto.DtoAggregator;
import com.github.stoynko.easydoc.web.dto.DtoContext;
import com.github.stoynko.easydoc.appointment.web.dto.request.AppointmentRequest;
import com.github.stoynko.easydoc.user.web.dto.request.LoginRequest;
import com.github.stoynko.easydoc.user.web.dto.request.RegisterRequest;
import com.github.stoynko.easydoc.user.web.dto.request.SubmitAccountDetailsRequest;
import com.github.stoynko.easydoc.web.model.ViewFragment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.github.stoynko.easydoc.practitioner.web.mapper.PractitionerMapper.toDoctorBriefInfoFrom;
import static com.github.stoynko.easydoc.practitioner.web.mapper.PractitionerMapper.toDoctorDetailedInfoFrom;
import static com.github.stoynko.easydoc.practitioner.web.mapper.PractitionerMapper.toRegisterPractitionerFrom;
import static com.github.stoynko.easydoc.user.model.AccountRole.PATIENT;
import static com.github.stoynko.easydoc.user.web.mapper.UserMapper.toUserSummary;

@Component
@RequiredArgsConstructor
public class PatientRoleViewResolver implements RoleViewResolver {

    private final DtoAggregator dtoAggregator;
    private final UserService userService;
    private final DoctorService doctorService;
    private final AppointmentService appointmentService;
    private final PractitionerApplicationService practitionerApplicationService;

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

            case PRESCRIPTION_VIEW -> {

            }
            case PRESCRIPTIONS_TABLE -> model.put("prescriptions", null);

            case APPOINTMENTS_TABLE -> {
                model.put("upcomingAppointments", appointmentService.getPatientUpcomingAppointments(dtoContext.principal().getId())
                        .stream().map(AppointmentMapper::toPatientAppointmentDetailsFrom)
                        .collect(Collectors.toList()));

                model.put("pastAppointments", appointmentService.getPatientPastAppointments(dtoContext.principal().getId())
                        .stream().map(AppointmentMapper::toPatientAppointmentDetailsFrom)
                        .collect(Collectors.toList()));
            }

            case APPOINTMENT_CREATION -> {
                model.put("processState", "formEntry");
                Doctor doctor = doctorService.getDoctorByDoctorId(dtoContext.resourceId());
                model.putIfAbsent("doctorSummary", toDoctorBriefInfoFrom(doctor));

                AppointmentRequest appointmentRequest = new AppointmentRequest();
                appointmentRequest.setDate(LocalDate.now());
                model.putIfAbsent("appointmentRequest", appointmentRequest);
                model.putIfAbsent("timeSlots", appointmentService.getTimeSlotsFor(dtoContext.resourceId(), appointmentRequest.getDate()));
            }

            case ACCOUNT -> {
                Doctor doctor = doctorService.getDoctorByDoctorId(dtoContext.resourceId());
                model.put("doctorSummary", toDoctorDetailedInfoFrom(doctor));
            }

            case DOCTOR_ONBOARDING -> {
                User user = userService.getUserById(dtoContext.principal().getId());
                model.put("processState", "formEntry");
                model.putIfAbsent("registerPractitionerRequest", toRegisterPractitionerFrom(user));
            }

            case SETTINGS -> {
                ViewFragment viewFragment = dtoContext.viewFragment();

                if (dtoContext.principal() != null) {
                    dtoAggregator.aggregateDtoForFragment(model, viewFragment, dtoContext.principal().getId());
                }
            }
        }

        if (dtoContext.principal() != null) {
            model.put("userSummary", toUserSummary(userService.getUserById(dtoContext.principal().getId())));
            model.put("hasSubmittedApplication", practitionerApplicationService.hasPendingApplication(dtoContext.principal().getId()));
        }

        return model;
    }
}