package com.github.stoynko.easydoc.web.dto.response;

import com.github.stoynko.easydoc.models.enums.Gender;
import lombok.Setter;
import lombok.Getter;
import lombok.Builder;
import lombok.AllArgsConstructor;

import java.util.UUID;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class PatientSummaryResponse {

    private UUID id;

    private String patientPin;

    private String emailAddress;

    private String profilePhotoUrl;

    private String firstName;

    private String lastName;

    private Gender gender;
}