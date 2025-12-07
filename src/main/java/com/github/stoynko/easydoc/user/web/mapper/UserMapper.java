package com.github.stoynko.easydoc.user.web.mapper;

import com.github.stoynko.easydoc.appointment.web.dto.response.PatientSummaryResponse;
import com.github.stoynko.easydoc.user.model.User;

import com.github.stoynko.easydoc.practitioner.web.dto.request.RegisterPractitionerRequest;
import com.github.stoynko.easydoc.user.web.dto.request.RegisterRequest;

import java.util.HashSet;

import com.github.stoynko.easydoc.user.web.dto.request.UpdateAccountDetailsRequest;
import com.github.stoynko.easydoc.user.web.dto.request.UpdateContactDetailsRequest;
import com.github.stoynko.easydoc.user.web.dto.request.UpdateEmailAddressRequest;
import com.github.stoynko.easydoc.user.web.dto.response.UserSummaryResponse;
import lombok.experimental.UtilityClass;

import static com.github.stoynko.easydoc.user.model.AccountAuthority.CAN_BOOK_APPOINTMENT;
import static com.github.stoynko.easydoc.user.model.AccountRole.PATIENT;
import static com.github.stoynko.easydoc.user.model.AccountStatus.INCOMPLETE;
import static com.github.stoynko.easydoc.utilities.GenerationalUtilities.extractName;

@UtilityClass
public class UserMapper {

    public static User toUserEntity(RegisterRequest request, String hashedPassword) {
        return User.builder()
                .emailAddress(request.getEmailAddress())
                .passwordHash(hashedPassword)
                .accountStatus(INCOMPLETE)
                .emailVerified(false)
                .profileCompleted(false)
                .role(PATIENT)
                .authority(new HashSet<>())
                .build();
    }

    public static UpdateAccountDetailsRequest toPersonalDetailsFrom(User user) {
        return UpdateAccountDetailsRequest.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .dateOfBirth(user.getDateOfBirth())
                .gender(user.getGender())
                .build();
    }

    public static UpdateContactDetailsRequest toContactDetailsFrom(User user) {
        return UpdateContactDetailsRequest.builder()
                .phoneNumber(user.getPhoneNumber())
                .build();
    }

    public static UpdateEmailAddressRequest toEmailAddressFrom(User user) {
        return UpdateEmailAddressRequest.builder()
                .currentEmailAddress(user.getEmailAddress())
                .build();
    }

    public static PatientSummaryResponse toUserSummary(User user) {
        return PatientSummaryResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName() != null ? user.getFirstName() : "")
                .lastName(user.getLastName() != null ? user.getLastName() : "")
                .profilePhotoUrl("")
                .gender(user.getGender())
                .build();
    }

    public static UpdateAccountDetailsRequest toChangePersonalDetailsFrom(RegisterPractitionerRequest request) {
        return UpdateAccountDetailsRequest.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .build();
    }

    public static UserSummaryResponse toUserSummaryFrom(User user) {
        return UserSummaryResponse.builder()
                .id(user.getId())
                .name(extractName(user))
                .accountRole(user.getRole())
                .accountStatus(user.getAccountStatus())
                .emailAddress(user.getEmailAddress())
                .canBookAppointment(user.getAuthority().contains(CAN_BOOK_APPOINTMENT))
                .creationDate(user.getCreatedModifiedAt().getCreatedAt())
                .updatedDate(user.getCreatedModifiedAt().getUpdatedAt())
                .build();
    }
}
