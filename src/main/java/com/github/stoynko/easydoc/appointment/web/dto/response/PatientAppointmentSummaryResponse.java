package com.github.stoynko.easydoc.appointment.web.dto.response;

import com.github.stoynko.easydoc.appointment.model.AppointmentReason;
import com.github.stoynko.easydoc.practitioner.model.Expertise;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class PatientAppointmentSummaryResponse {

    private String appointmentId;

    private String appointmentPublicId;

    private String doctorUin;

    private String doctorFirstName;

    private String doctorLastName;

    private Expertise doctorExpertise;

    private String doctorEmail;

    private String doctorPhoneNumber;

    private AppointmentReason appointmentReason;

    private String additionalNotes;

    private LocalDateTime appointmentDate;
}
