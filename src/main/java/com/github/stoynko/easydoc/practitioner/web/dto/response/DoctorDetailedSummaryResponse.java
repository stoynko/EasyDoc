package com.github.stoynko.easydoc.practitioner.web.dto.response;

import com.github.stoynko.easydoc.practitioner.model.Expertise;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DoctorDetailedSummaryResponse {

    private UUID id;

    private String firstName;

    private String lastName;

    private String emailAddress;

    private String phoneNumber;

    private String profilePhotoUrl;

    private String uin;

    private Expertise expertise;

    private int yearsExperience;

    private String practiceLocation;

    private String professionalHighlights;

    private String spokenLanguages;

}
