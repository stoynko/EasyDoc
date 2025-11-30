package com.github.stoynko.easydoc.web.mappers;

import com.github.stoynko.easydoc.client.dto.UpsertPrescriptionRequest;
import com.github.stoynko.easydoc.models.Appointment;
import com.github.stoynko.easydoc.models.Doctor;
import com.github.stoynko.easydoc.models.PractitionerApplication;
import com.github.stoynko.easydoc.models.Report;
import com.github.stoynko.easydoc.models.User;

import com.github.stoynko.easydoc.models.enums.DocumentStatus;
import com.github.stoynko.easydoc.web.dto.request.AppointmentRequest;
import com.github.stoynko.easydoc.web.dto.request.MedicalReportRequest;
import com.github.stoynko.easydoc.web.dto.request.RegisterPractitionerRequest;
import com.github.stoynko.easydoc.web.dto.request.RegisterRequest;
import com.github.stoynko.easydoc.web.dto.response.CloudinaryUploadResult;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import lombok.experimental.UtilityClass;

import static com.github.stoynko.easydoc.models.enums.AccountRole.PATIENT;
import static com.github.stoynko.easydoc.models.enums.AccountStatus.INCOMPLETE;
import static com.github.stoynko.easydoc.models.enums.ApplicationStatus.PENDING;
import static com.github.stoynko.easydoc.models.enums.AppointmentStatus.CONFIRMED;
import static com.github.stoynko.easydoc.models.enums.DocumentStatus.ISSUED;
import static com.github.stoynko.easydoc.utilities.GenerationalUtilities.extractDigits;

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
                .authority(new HashSet<>())
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

    public static Appointment toAppointmentEntity(AppointmentRequest request, User patient, Doctor doctor) {
        return Appointment.builder()
                .publicId(extractDigits(UUID.randomUUID().toString()))
                .patient(patient)
                .doctor(doctor)
                .startsAt(LocalDateTime.of(request.getDate(), request.getTime()))
                .appointmentReason(request.getAppointmentReason())
                .additionalNotes(request.getAdditionalNotes())
                .status(CONFIRMED)
                .build();
    }

    public static Report ToReportEntity(MedicalReportRequest request, Appointment appointment) {
        return Report.builder()
                .publicId(extractDigits(UUID.randomUUID().toString()))
                .appointment(appointment)
                .documentStatus(ISSUED)
                .accompanyingIllnesses(request.getAccompanyingIllnesses())
                .anamnesis(request.getAnamnesis())
                .statusAtExam(request.getStatusAtExam())
                .clinicalFindings(request.getClinicalFindings())
                .careRecommendations(request.getCareRecommendations())
                .medicamentTreatment(request.getMedicamentTreatment())
                .diagnosis(request.getDiagnosis())
                .build();
    }

    public static UpsertPrescriptionRequest toPrescriptionRequest(UUID appointmentId) {
        return UpsertPrescriptionRequest.builder()
                .appointmentId(appointmentId)
                .medicaments(new ArrayList<>())
                .build();
    }
}
