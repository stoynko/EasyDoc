package com.github.stoynko.easydoc.report.web.dto.response;

import com.github.stoynko.easydoc.report.model.ReportStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class MedicalReportResponse {

    private ReportStatus reportStatus;

    private String diagnosis;

    private String icdCode;

    private String anamnesis;

    private String statusAtExam;

    private String accompanyingIllnesses;

    private String clinicalFindings;

    private String careRecommendations;

    private String medicamentTreatment;

    private LocalDateTime createdAt;

    private LocalDateTime issuedAt;

}
