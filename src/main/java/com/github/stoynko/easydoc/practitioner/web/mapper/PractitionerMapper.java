package com.github.stoynko.easydoc.practitioner.web.mapper;

import com.github.stoynko.easydoc.media.dto.CloudinaryUploadResult;
import com.github.stoynko.easydoc.practitioner.model.Doctor;
import com.github.stoynko.easydoc.practitioner.model.PractitionerApplication;
import com.github.stoynko.easydoc.practitioner.web.dto.request.RegisterPractitionerRequest;
import com.github.stoynko.easydoc.practitioner.web.dto.request.UpdateProfessionalDetailsRequest;
import com.github.stoynko.easydoc.practitioner.web.dto.response.DoctorBriefSummaryResponse;
import com.github.stoynko.easydoc.practitioner.web.dto.response.DoctorDetailedSummaryResponse;
import com.github.stoynko.easydoc.practitioner.web.dto.response.PendingPractitionerApplicationResponse;
import com.github.stoynko.easydoc.user.model.User;
import lombok.experimental.UtilityClass;

import static com.github.stoynko.easydoc.practitioner.model.PractitionerApplicationStatus.PENDING;

@UtilityClass
public class PractitionerMapper {

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

    public static RegisterPractitionerRequest toRegisterPractitionerFrom(User user) {
        return RegisterPractitionerRequest.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .dateOfBirth(user.getDateOfBirth())
                .gender(user.getGender())
                .build();
    }

    public static UpdateProfessionalDetailsRequest toProfessionalDetailsFrom(Doctor doctor) {
        return UpdateProfessionalDetailsRequest.builder()
                .expertise(doctor.getExpertise())
                .yearsExperience(doctor.getYearsExperience())
                .practiceLocation(doctor.getPracticeLocation())
                .professionalHighlights(doctor.getProfessionalHighlights())
                .build();
    }

    public static DoctorBriefSummaryResponse toDoctorBriefInfoFrom(Doctor doctor) {
        return DoctorBriefSummaryResponse.builder()
                .id(doctor.getId())
                .firstName(doctor.getUser().getFirstName())
                .lastName(doctor.getUser().getLastName())
                .profilePhotoUrl(doctor.getProfilePhotoUrl())
                .expertise(doctor.getExpertise())
                .practiceLocation(doctor.getPracticeLocation())
                .build();
    }

    public static DoctorDetailedSummaryResponse toDoctorDetailedInfoFrom(Doctor doctor) {
        return DoctorDetailedSummaryResponse.builder()
                .id(doctor.getId())
                .firstName(doctor.getUser().getFirstName())
                .lastName(doctor.getUser().getLastName())
                .emailAddress(doctor.getUser().getEmailAddress())
                .phoneNumber(doctor.getUser().getPhoneNumber())
                .profilePhotoUrl(doctor.getProfilePhotoUrl())
                .uin(doctor.getUin())
                .expertise(doctor.getExpertise())
                .yearsExperience(doctor.getYearsExperience())
                .practiceLocation(doctor.getPracticeLocation())
                .professionalHighlights(doctor.getProfessionalHighlights())
                .spokenLanguages(doctor.getSpokenLanguages())
                .build();
    }

    public static PendingPractitionerApplicationResponse toPendingPractitionerApplicationFrom(PractitionerApplication application) {

        return PendingPractitionerApplicationResponse.builder()
                .applicationId(application.getId())
                .profilePhotoUrl(application.getProfilePhotoUrl())
                .doctorUin(application.getUin())
                .doctorExpertise(application.getExpertise())
                .doctorYearsExperience(application.getYearsExperience())
                .doctorProfessionalHighlights(application.getProfessionalHighlights())
                .doctorPracticeLocation(application.getPracticeLocation())
                .doctorSpokenLanguages(application.getSpokenLanguages())
                .build();
    }
}
