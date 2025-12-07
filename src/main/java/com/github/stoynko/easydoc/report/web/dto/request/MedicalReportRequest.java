package com.github.stoynko.easydoc.report.web.dto.request;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class MedicalReportRequest {

    private UUID appointmentId;

    @NotBlank
    @Size(max = 100, message = "Please input up to 100 characters")
    private String diagnosis;

    @NotBlank(message = "Please input an anamnesis")
    @Size(max = 500, message = "Please input up to 500 characters")
    private String anamnesis;

    @NotBlank(message = "Please input the status at examination")
    @Size(max = 500, message = "Please input up to 500 characters")
    private String statusAtExam;

    @Size(max = 200, message = "Please input up to 500 characters")
    private String accompanyingIllnesses;

    @Size(max = 1000, message = "Please input up to 500 characters")
    private String clinicalFindings;

    @Size(max = 500, message = "Please input up to 500 characters")
    private String careRecommendations;

    @Size(max = 500, message = "Please input up to 500 characters")
    private String medicamentTreatment;
}
