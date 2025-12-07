package com.github.stoynko.easydoc.appointment.web.dto.response;

import com.github.stoynko.easydoc.appointment.model.AppointmentReason;
import com.github.stoynko.easydoc.appointment.model.AppointmentStatus;
import com.github.stoynko.easydoc.practitioner.model.Expertise;
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
public class PatientAppointmentResponse {

    private UUID appointmentId;

    private String appointmentPublicId;

    private String doctorUin;

    private String doctorFirstName;

    private String doctorLastName;

    private Expertise doctorExpertise;

    private AppointmentReason appointmentReason;

    private AppointmentStatus appointmentStatus;

    private LocalDateTime startsAt;

    private boolean hasReport;

    private boolean hasPrescription;

}
