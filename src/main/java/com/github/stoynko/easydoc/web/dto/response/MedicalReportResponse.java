package com.github.stoynko.easydoc.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class MedicalReportResponse {

    private String diagnosis;

    private String icdCode;

    private String anamnesis;

    private String statusAtExam;

    private String accompanyingIllnesses;

    private String clinicalFindings;

    private String careRecommendations;

    private String medicamentTreatment;
}
