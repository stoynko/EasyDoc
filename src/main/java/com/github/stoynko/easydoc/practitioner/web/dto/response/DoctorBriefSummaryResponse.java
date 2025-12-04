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
public class DoctorBriefSummaryResponse {

    private UUID id;

    private String firstName;

    private String lastName;

    private String profilePhotoUrl;

    private Expertise expertise;

    private String practiceLocation;
}
