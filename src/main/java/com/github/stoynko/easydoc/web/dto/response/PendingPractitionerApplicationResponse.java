package com.github.stoynko.easydoc.web.dto.response;

import com.github.stoynko.easydoc.models.enums.Expertise;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class PendingPractitionerApplicationResponse {

    private UUID applicationId;

    private String profilePhotoUrl;

    private String doctorUin;

    private Expertise doctorExpertise;

    private String doctorYearsExperience;

    private String doctorPracticeLocation;

    private String doctorProfessionalHighlights;

    private String doctorSpokenLanguages;
}
