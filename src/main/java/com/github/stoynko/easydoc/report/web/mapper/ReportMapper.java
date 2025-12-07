package com.github.stoynko.easydoc.report.web.mapper;

import com.github.stoynko.easydoc.appointment.model.Appointment;
import com.github.stoynko.easydoc.report.model.Report;
import com.github.stoynko.easydoc.report.web.dto.request.MedicalReportRequest;
import com.github.stoynko.easydoc.report.web.dto.response.MedicalReportResponse;
import lombok.experimental.UtilityClass;

import java.util.UUID;

import static com.github.stoynko.easydoc.report.model.ReportStatus.DRAFT;
import static com.github.stoynko.easydoc.report.model.ReportStatus.ISSUED;
import static com.github.stoynko.easydoc.utilities.GenerationalUtilities.extractDigits;

@UtilityClass
public class ReportMapper {

    public static Report toReportEntity(MedicalReportRequest request, Appointment appointment) {
        return Report.builder()
                .publicId(extractDigits(UUID.randomUUID().toString()))
                .appointment(appointment)
                .reportStatus(DRAFT)
                .accompanyingIllnesses(request.getAccompanyingIllnesses())
                .anamnesis(request.getAnamnesis())
                .statusAtExam(request.getStatusAtExam())
                .clinicalFindings(request.getClinicalFindings())
                .careRecommendations(request.getCareRecommendations())
                .medicamentTreatment(request.getMedicamentTreatment())
                .diagnosis(request.getDiagnosis())
                .build();
    }

    public static MedicalReportResponse toMedicalReportResponseFrom(Report report) {
        return MedicalReportResponse.builder()
                .reportStatus(report.getReportStatus())
                .diagnosis(report.getDiagnosis())
                .anamnesis(report.getAnamnesis())
                .statusAtExam(report.getStatusAtExam())
                .accompanyingIllnesses(report.getAccompanyingIllnesses())
                .clinicalFindings(report.getClinicalFindings())
                .careRecommendations(report.getCareRecommendations())
                .medicamentTreatment(report.getMedicamentTreatment())
                .createdAt(report.getCreatedModifiedAt().getCreatedAt())
                .issuedAt(report.getIssuedAt())
                .build();
    }
}
