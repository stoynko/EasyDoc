package com.github.stoynko.easydoc.appointment.web.dto.response;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class PatientBriefSummaryResponse {

    private UUID id;

    private String patientPin;

    private String firstName;

    private String lastName;
}