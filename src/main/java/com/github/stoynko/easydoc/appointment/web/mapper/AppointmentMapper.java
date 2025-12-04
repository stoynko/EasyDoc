package com.github.stoynko.easydoc.appointment.web.mapper;

import com.github.stoynko.easydoc.appointment.model.Appointment;
import com.github.stoynko.easydoc.appointment.web.dto.request.AppointmentRequest;
import com.github.stoynko.easydoc.appointment.web.dto.response.DoctorAppointmentResponse;
import com.github.stoynko.easydoc.appointment.web.dto.response.DoctorAppointmentSummaryResponse;
import com.github.stoynko.easydoc.appointment.web.dto.response.PatientAppointmentResponse;
import com.github.stoynko.easydoc.appointment.web.dto.response.PatientAppointmentSummaryResponse;
import com.github.stoynko.easydoc.practitioner.model.Doctor;
import com.github.stoynko.easydoc.user.model.User;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.github.stoynko.easydoc.appointment.model.AppointmentStatus.PENDING;
import static com.github.stoynko.easydoc.report.model.ReportStatus.ISSUED;
import static com.github.stoynko.easydoc.utilities.GenerationalUtilities.extractDigits;
import static com.github.stoynko.easydoc.utilities.GenerationalUtilities.extractName;

@UtilityClass
public class AppointmentMapper {

    public static Appointment toAppointmentEntity(AppointmentRequest request, User patient, Doctor doctor) {
        return Appointment.builder()
                .publicId(extractDigits(UUID.randomUUID().toString()))
                .patient(patient)
                .doctor(doctor)
                .startsAt(LocalDateTime.of(request.getDate(), request.getTime()))
                .appointmentReason(request.getAppointmentReason())
                .additionalNotes(request.getAdditionalNotes())
                .status(PENDING)
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
                .hasReport(appointment.hasReport() && appointment.getReport().getReportStatus() == ISSUED)
                .hasPrescription(appointment.hasPrescription())
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
                .hasIssuedReport(appointment.hasReport() && appointment.getReport().getReportStatus() == ISSUED ? true : false)
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
                .patientPhoneNumber(patient.getPhoneNumber())
                .patientFirstName(patient.getFirstName())
                .patientLastName(patient.getLastName())
                .appointmentReason(appointment.getAppointmentReason())
                .appointmentDate(appointment.getStartsAt())
                .additionalNotes(appointment.getAdditionalNotes())
                .build();
    }

    public static PatientAppointmentSummaryResponse toPatientAppointmentSummaryResponse(Appointment appointment) {

        Doctor doctor = appointment.getDoctor();
        User doctorUserProfile = doctor.getUser();

        return PatientAppointmentSummaryResponse.builder()
                .appointmentId(appointment.getId().toString())
                .appointmentPublicId(appointment.getPublicId())
                .doctorUin(doctor.getUin())
                .doctorFirstName(doctorUserProfile.getFirstName())
                .doctorLastName(doctorUserProfile.getLastName())
                .doctorExpertise(doctor.getExpertise())
                .doctorEmail(doctorUserProfile.getEmailAddress())
                .doctorPhoneNumber(doctorUserProfile.getPhoneNumber())
                .appointmentReason(appointment.getAppointmentReason())
                .appointmentDate(appointment.getStartsAt())
                .additionalNotes(appointment.getAdditionalNotes())
                .build();
    }
}
