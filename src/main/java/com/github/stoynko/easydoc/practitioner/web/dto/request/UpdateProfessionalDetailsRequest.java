package com.github.stoynko.easydoc.practitioner.web.dto.request;

import com.github.stoynko.easydoc.practitioner.model.Expertise;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProfessionalDetailsRequest {

    @Size(max = 100, message = "Please input up to 100 characters")
    private String spokenLanguages;

    @NotNull (message = "Please select your area of expertise")
    private Expertise expertise;

    @NotNull (message = "Please input your years of experience")
    private int yearsExperience;

    @NotBlank (message = "Please enter the address of your practice location")
    private String practiceLocation;

    @NotBlank (message = "Please provide details of your professional highlights.")
    @Size(max = 5000, message = "Your professional highlights must be at most 5000 characters.")
    private String professionalHighlights;
}
