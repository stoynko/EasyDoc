package com.github.stoynko.easydoc.web.mappers;

import com.github.stoynko.easydoc.models.Doctor;
import com.github.stoynko.easydoc.models.PractitionerApplication;
import com.github.stoynko.easydoc.models.User;

import com.github.stoynko.easydoc.web.dto.request.RegisterPractitionerRequest;
import com.github.stoynko.easydoc.web.dto.request.RegisterRequest;
import com.github.stoynko.easydoc.web.dto.response.CloudinaryUploadResult;
import lombok.experimental.UtilityClass;

import static com.github.stoynko.easydoc.models.enums.AccountRole.PATIENT;
import static com.github.stoynko.easydoc.models.enums.AccountStatus.INCOMPLETE;
import static com.github.stoynko.easydoc.models.enums.ApplicationStatus.PENDING;

@UtilityClass
public class EntityMapper {

    public static User toUserEntity(RegisterRequest request, String hashedPassword) {
        return User.builder()
                .emailAddress(request.getEmailAddress())
                .passwordHash(hashedPassword)
                .accountStatus(INCOMPLETE)
                .emailVerified(false)
                .profileCompleted(false)
                .role(PATIENT)
                .build();
    }

    public static Doctor toDoctorEntity(PractitionerApplication application) {
        return Doctor.builder()
                .user(application.getUser())
                .profilePhotoUrl(application.getProfilePhotoUrl())
                .profilePhotoPublicId(application.getProfilePhotoPublicId())
                .uin(application.getUin())
                .expertise(application.getExpertise())
                .yearsExperience(application.getYearsExperience())
                .practiceLocation(application.getPracticeLocation())
                .professionalHighlights(application.getProfessionalHighlights())
                .spokenLanguages(application.getSpokenLanguages())
                .build();
    }

    public static PractitionerApplication toPractitionerApplicationEntity(User user, RegisterPractitionerRequest request, CloudinaryUploadResult uploadResult) {
        return PractitionerApplication.builder()
                .user(user)
                .profilePhotoUrl(uploadResult.url())
                .profilePhotoPublicId(uploadResult.publicId())
                .uin(request.getUin())
                .expertise(request.getExpertise())
                .yearsExperience(request.getYearsExperience())
                .practiceLocation(request.getPracticeLocation())
                .professionalHighlights(request.getProfessionalHighlights())
                .spokenLanguages(request.getSpokenLanguages())
                .applicationStatus(PENDING)
                .build();
    }
}
