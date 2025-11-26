package com.github.stoynko.easydoc.web.mappers;

import com.github.stoynko.easydoc.models.Appointment;
import com.github.stoynko.easydoc.models.Doctor;
import com.github.stoynko.easydoc.models.PractitionerApplication;
import com.github.stoynko.easydoc.models.Report;
import com.github.stoynko.easydoc.models.User;
import com.github.stoynko.easydoc.models.enums.AppointmentReason;
import com.github.stoynko.easydoc.web.dto.request.UpdateContactDetailsRequest;
import com.github.stoynko.easydoc.web.dto.request.UpdateEmailAddressRequest;
import com.github.stoynko.easydoc.web.dto.request.UpdateAccountDetailsRequest;
import com.github.stoynko.easydoc.web.dto.request.UpdateProfessionalDetailsRequest;
import com.github.stoynko.easydoc.web.dto.request.RegisterPractitionerRequest;
import com.github.stoynko.easydoc.web.dto.response.DoctorAppointmentResponse;
import com.github.stoynko.easydoc.web.dto.response.DoctorAppointmentSummaryResponse;
import com.github.stoynko.easydoc.web.dto.response.DoctorBriefSummaryResponse;
import com.github.stoynko.easydoc.web.dto.response.DoctorDetailedSummaryResponse;
import com.github.stoynko.easydoc.web.dto.response.MedicalReportResponse;
import com.github.stoynko.easydoc.web.dto.response.PatientAppointmentResponse;
import com.github.stoynko.easydoc.web.dto.response.PatientSummaryResponse;
import com.github.stoynko.easydoc.web.dto.response.PendingPractitionerApplicationResponse;
import com.github.stoynko.easydoc.web.dto.response.UserSummaryResponse;
import java.time.Instant;
import lombok.experimental.UtilityClass;

import static com.github.stoynko.easydoc.models.enums.AccountAuthority.CAN_BOOK_APPOINTMENT;
import static com.github.stoynko.easydoc.utilities.ValidationUtilities.extractName;

@UtilityClass
public class DtoMapper {

    public UpdateAccountDetailsRequest toPersonalDetailsFrom(User user) {
        return UpdateAccountDetailsRequest.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .dateOfBirth(user.getDateOfBirth())
                .gender(user.getGender())
                .build();
    }

    public UpdateContactDetailsRequest toContactDetailsFrom(User user) {
        return UpdateContactDetailsRequest.builder()
                .phoneNumber(user.getPhoneNumber())
                .build();
    }

    public UpdateEmailAddressRequest toEmailAddressFrom(User user) {
        return UpdateEmailAddressRequest.builder()
                .currentEmailAddress(user.getEmailAddress())
                .build();
    }

    public RegisterPractitionerRequest toRegisterPractitionerFrom(User user) {
        return RegisterPractitionerRequest.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .dateOfBirth(user.getDateOfBirth())
                .gender(user.getGender())
                .build();
    }

    public PatientSummaryResponse toUserSummary(User user) {
        return PatientSummaryResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName() != null ? user.getFirstName() : "")
                .lastName(user.getLastName() != null ? user.getLastName() : "")
                .profilePhotoUrl("")
                .gender(user.getGender())
                .build();
    }

    public UpdateProfessionalDetailsRequest toProfessionalDetailsFrom(Doctor doctor) {
        return UpdateProfessionalDetailsRequest.builder()
                .expertise(doctor.getExpertise())
                .yearsExperience(doctor.getYearsExperience())
                .practiceLocation(doctor.getPracticeLocation())
                .professionalHighlights(doctor.getProfessionalHighlights())
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

    public PatientSummaryResponse toUserDetailsFrom(User user) {
        return PatientSummaryResponse.builder()
                .id(user.getId())
                .emailAddress(user.getEmailAddress())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }

    public UserSummaryResponse toUserSummaryFrom(User user) {
        return UserSummaryResponse.builder()
                .id(user.getId())
                .name(extractName(user))
                .accountRole(user.getRole())
                .accountStatus(user.getAccountStatus())
                .emailAddress(user.getEmailAddress())
                .canBookAppointment(user.getAuthority().contains(CAN_BOOK_APPOINTMENT))
                .creationDate(user.getCreatedModifiedAt().getCreatedAt())
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

    public static PatientAppointmentResponse toPatientAppointmentDetailsFrom(Appointment appointment) {
        return PatientAppointmentResponse.builder()
                .appointmentId(appointment.getId())
                .appointmentPublicId(appointment.getPublicId())
                .doctorUin(appointment.getDoctor().getUin())
                .doctorFirstName(appointment.getDoctor().getUser().getFirstName())
                .doctorLastName(appointment.getDoctor().getUser().getLastName())
                .doctorExpertise(appointment.getDoctor().getExpertise())
                .appointmentReason(appointment.getAppointmentReason())
                .appointmentStatus(appointment.getStatus())
                .startsAt(appointment.getStartsAt())
                .build();
    }

    public static DoctorAppointmentResponse toDoctorAppointmentDetailsFrom(Appointment appointment) {

        User patient = appointment.getPatient();

        return DoctorAppointmentResponse.builder()
                .appointmentId(appointment.getId())
                .appointmentPublicId(appointment.getPublicId())
                .patientPin(appointment.getPatient().getPersonalIdentificationNumber())
                .patientName(extractName(patient))
                .patientEmailAddress(patient.getEmailAddress())
                .appointmentReason(appointment.getAppointmentReason())
                .appointmentStatus(appointment.getStatus())
                .appointmentAdditionalNotes(appointment.getAdditionalNotes())
                .hasReport(appointment.hasReport())
                .hasPrescription(appointment.hasPrescription())
                .startsAt(appointment.getStartsAt())
                .build();
    }

    public static DoctorAppointmentSummaryResponse toDoctorAppointmentSummaryResponse(Appointment appointment) {

        User patient = appointment.getPatient();

        return DoctorAppointmentSummaryResponse.builder()
                .appointmentId(appointment.getId().toString())
                .appointmentPublicId(appointment.getPublicId())
                .patientPin(patient.getPersonalIdentificationNumber())
                .patientEmail(patient.getEmailAddress())
                .patientFirstName(patient.getFirstName())
                .patientLastName(patient.getLastName())
                .appointmentReason(appointment.getAppointmentReason())
                .appointmentDate(appointment.getStartsAt())
                .additionalNotes(appointment.getAdditionalNotes())
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

    public static MedicalReportResponse toMedicalReportRequestFrom(Report medicalReport) {
        return MedicalReportResponse.builder()


                .build();
    }
}
