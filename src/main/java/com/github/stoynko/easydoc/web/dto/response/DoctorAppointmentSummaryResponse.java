package com.github.stoynko.easydoc.web.dto.response;

import com.github.stoynko.easydoc.models.enums.AppointmentReason;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DoctorAppointmentSummaryResponse {

    private String patientPublicId;

    private String patientPin;

    private String patientEmail;

    private AppointmentReason appointmentReason;

    private Instant appointmentDate;
}
