package com.github.stoynko.easydoc.web.dto;

import com.github.stoynko.easydoc.services.AppointmentService;
import com.github.stoynko.easydoc.services.DoctorService;
import com.github.stoynko.easydoc.services.UserService;
import com.github.stoynko.easydoc.web.dto.request.UpdateProfilePhotoRequest;
import com.github.stoynko.easydoc.web.dto.request.UpdatePasswordRequest;
import com.github.stoynko.easydoc.web.dto.request.DeleteAccountRequest;
import com.github.stoynko.easydoc.web.mappers.DtoMapper;
import com.github.stoynko.easydoc.web.model.ViewFragment;

import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import static com.github.stoynko.easydoc.web.model.ViewFragment.DELETE_ACCOUNT;
import static com.github.stoynko.easydoc.web.model.ViewFragment.PERSONAL_INFO;
import static com.github.stoynko.easydoc.web.model.ViewFragment.PROFESSIONAL_INFO;
import static com.github.stoynko.easydoc.web.model.ViewFragment.SECURITY;
import static com.github.stoynko.easydoc.web.model.ViewFragment.SETTINGS_DASHBOARD;

@Component
public class DtoAggregator {

    public static final String MODEL_ELEMENT_FRAGMENT = "fragmentPath";
    private final UserService userService;
    private final DoctorService doctorService;
    private final AppointmentService appointmentService;

    @Autowired
    public DtoAggregator(UserService userService, DoctorService doctorService, AppointmentService appointmentService) {
        this.userService = userService;
        this.doctorService = doctorService;
        this.appointmentService = appointmentService;
    }

    public Map<String, Object> aggregateDtoForFragment(Map<String, Object> model, ViewFragment viewFragment, UUID resourceId) {

        switch (viewFragment) {

            case SETTINGS_DASHBOARD -> {
                model.put(MODEL_ELEMENT_FRAGMENT, SETTINGS_DASHBOARD.getFragmentPath());
            }
            case PERSONAL_INFO -> {
                model.put(MODEL_ELEMENT_FRAGMENT, PERSONAL_INFO.getFragmentPath());
                model.putIfAbsent("changeAvatarRequest", new UpdateProfilePhotoRequest());
                model.putIfAbsent("changePersonalDetailsRequest", DtoMapper.toPersonalDetailsFrom(userService.getUserById(resourceId)));
                model.putIfAbsent("changeContactDetailsRequest", DtoMapper.toContactDetailsFrom(userService.getUserById(resourceId)));
            }
            case PROFESSIONAL_INFO -> {
                model.put(MODEL_ELEMENT_FRAGMENT, PROFESSIONAL_INFO.getFragmentPath());
                model.putIfAbsent("changeProfessionalDetailsRequest", DtoMapper.toProfessionalDetailsFrom(doctorService.getDoctorDetailsByUserId(resourceId)));
            }

            case SECURITY -> {
                model.put(MODEL_ELEMENT_FRAGMENT, SECURITY.getFragmentPath());
                model.putIfAbsent("changeEmailAddressRequest", DtoMapper.toEmailAddressFrom(userService.getUserById(resourceId)));
                model.putIfAbsent("changePasswordRequest", new UpdatePasswordRequest());
            }

            case PREFERENCES, MEMBERSHIP -> model.put(MODEL_ELEMENT_FRAGMENT, PERSONAL_INFO.getFragmentPath());

            case DELETE_ACCOUNT -> {
                model.put(MODEL_ELEMENT_FRAGMENT, DELETE_ACCOUNT.getFragmentPath());
                model.putIfAbsent("deleteAccountRequest", new DeleteAccountRequest());
            }

            case DOCTOR_ONBOARDING -> {
            model.putIfAbsent("registerPractitionerRequest", DtoMapper.toRegisterPractitionerFrom(userService.getUserById(resourceId)));
            model.put("processState", "formEntry");
            }
        }
        return model;
    }
}
