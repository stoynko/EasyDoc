package com.github.stoynko.easydoc.appointment.web.dto.response;

import com.github.stoynko.easydoc.appointment.model.AppointmentReason;
import com.github.stoynko.easydoc.appointment.model.AppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DoctorAppointmentResponse {

    private UUID appointmentId;

    private String appointmentPublicId;

    private String patientPin;

    private String patientName;

    private String patientEmailAddress;

    private AppointmentReason appointmentReason;

    private String appointmentAdditionalNotes;

    private AppointmentStatus appointmentStatus;

    private LocalDateTime startsAt;

    private boolean hasReport;

    private boolean hasIssuedReport;

    private boolean hasPrescription;

}
