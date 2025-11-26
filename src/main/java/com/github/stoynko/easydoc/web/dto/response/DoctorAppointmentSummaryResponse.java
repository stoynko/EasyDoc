package com.github.stoynko.easydoc.web.dto.response;

import com.github.stoynko.easydoc.models.enums.AppointmentReason;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class DoctorAppointmentSummaryResponse {

    private String appointmentId;

    private String appointmentPublicId;

    private String patientPublicId;

    private String patientPin;

    private String patientEmail;

    private String patientFirstName;

    private String patientLastName;

    private AppointmentReason appointmentReason;

    private String additionalNotes;

    private LocalDateTime appointmentDate;
}
