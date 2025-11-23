/*
package com.github.stoynko.hospital.web.controllers.advice;

import com.github.stoynko.hospital.mappers.DtoEnricher;
import com.github.stoynko.hospital.models.enums.AccountRole;
import com.github.stoynko.hospital.security.UserAuthenticationDetails;
import com.github.stoynko.hospital.services.DoctorService;
import com.github.stoynko.hospital.services.UserService;
import com.github.stoynko.hospital.web.dto.response.UserSummaryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import static com.github.stoynko.hospital.models.enums.AccountRole.DOCTOR;
import static com.github.stoynko.hospital.models.enums.AccountRole.PATIENT;

@ControllerAdvice
public class GlobalUserAdvice {

    private final UserService userService;

    @Autowired
    public GlobalUserAdvice(DoctorService doctorService, UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute
    public UserSummaryResponse getUserSummary(@AuthenticationPrincipal UserAuthenticationDetails principal) {

        if (principal == null) {
            return null;
        }

        if (ADMIN.equals(principal.getRole())) {
            return DtoEnricher.getEnrichedAdminSummary(adminService.getUserById(principal.getId()));
        } else if(DOCTOR.equals(principal.getRole())) {
            return DtoEnricher.getEnrichedDoctorSummary(doctorService.getUserById(principal.getId()));
        }

        return DtoEnricher.getEnrichedUserSummary(userService.getUserById(principal.getId()));
    }
}
*/
