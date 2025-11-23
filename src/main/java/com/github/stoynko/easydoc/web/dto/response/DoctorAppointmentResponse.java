package com.github.stoynko.easydoc.web.dto.response;

import com.github.stoynko.easydoc.models.enums.AppointmentReason;
import com.github.stoynko.easydoc.models.enums.AppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DoctorAppointmentResponse {

    private String appointmentPublicId;

    private String patientPin;

    private String patientName;

    private String patientEmailAddress;

    private AppointmentReason appointmentReason;

    private AppointmentStatus appointmentStatus;

    private LocalDateTime startsAt;
}
