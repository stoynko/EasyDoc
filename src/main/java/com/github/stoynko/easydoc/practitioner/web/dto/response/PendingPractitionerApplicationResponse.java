package com.github.stoynko.easydoc.practitioner.web.dto.response;

import com.github.stoynko.easydoc.practitioner.model.Expertise;
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

    private int doctorYearsExperience;

    private String doctorPracticeLocation;

    private String doctorProfessionalHighlights;

    private String doctorSpokenLanguages;
}
