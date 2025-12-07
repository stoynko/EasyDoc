package com.github.stoynko.easydoc.report.service;

import com.github.stoynko.easydoc.appointment.service.AppointmentService;
import com.github.stoynko.easydoc.report.client.DiagnosisClient;
import com.github.stoynko.easydoc.report.exception.ReportAlreadyExistsException;
import com.github.stoynko.easydoc.report.exception.ReportDoesNotExistException;
import com.github.stoynko.easydoc.appointment.model.Appointment;
import com.github.stoynko.easydoc.report.model.Report;
import com.github.stoynko.easydoc.report.model.ReportStatus;
import com.github.stoynko.easydoc.report.repository.ReportRepository;
import com.github.stoynko.easydoc.report.web.dto.request.MedicalReportRequest;

import com.github.stoynko.easydoc.report.web.dto.response.DiagnosisOptionResponse;
import com.github.stoynko.easydoc.report.web.dto.response.Icd10SearchResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.github.stoynko.easydoc.report.model.ReportStatus.DRAFT;
import static com.github.stoynko.easydoc.report.web.mapper.ReportMapper.toReportEntity;
import static com.github.stoynko.easydoc.report.model.ReportStatus.ISSUED;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportService {

    public static final int MAX_DIAGNOSE_ENTRIES = 50;

    private final ReportRepository repository;
    private final AppointmentService appointmentService;
    private final DiagnosisClient diagnosisClient;

    @Transactional
    public void createMedicalReport(Appointment appointment, MedicalReportRequest request) {

        if (appointment.hasReport()) {
            throw new ReportAlreadyExistsException();
        }

        Report report = toReportEntity(request, appointment);
        repository.save(report);

        appointment.setReport(report);
        appointmentService.saveAppointment(appointment);
        log.info("[medical-report-created] Medical report with id {} has been successfully created for appointment {}",  report.getId(), appointment.getId());
    }

    public void editMedicalReport(Report report, MedicalReportRequest request) {
        report.setAnamnesis(request.getAnamnesis());
        report.setStatusAtExam(request.getStatusAtExam());
        report.setAccompanyingIllnesses(request.getAccompanyingIllnesses());
        report.setClinicalFindings(request.getClinicalFindings());
        report.setCareRecommendations(request.getCareRecommendations());
        report.setMedicamentTreatment(request.getMedicamentTreatment());
        repository.save(report);
        log.info("[medical-report-created] Medical report with id {} has been successfully edited",  report.getId());
    }

    public void issueMedicalReport(Report report) {
        report.setReportStatus(ISSUED);
        report.setIssuedAt(LocalDateTime.now());
        repository.save(report);
        log.info("[medical-report-issued] Medical report with id {} has been successfully issued.",  report.getId());
    }

    public List<DiagnosisOptionResponse> findDiagnosisByKeyword(String keyword) {

        if (keyword.isBlank() || keyword.trim().isEmpty()) {
            return List.of();
        }

        try {
            Icd10SearchResponse response = diagnosisClient.searchIcd10("code,name", keyword, MAX_DIAGNOSE_ENTRIES);
            return response.rows().stream()
                    .map(row -> new DiagnosisOptionResponse(row.get(0) + " " + row.get(1)))
                    .collect(Collectors.toList());
        } catch (FeignException exception) {
            log.warn("[S2S Call Failed] Diagnosis ICD10 lookup failed for keyword '{}': {}", keyword, exception.toString());
            return List.of();
        } catch (Exception exception) {
            log.warn("[S2S Call Failed] Unexpected error during ICD10 lookup for keyword '{}'", keyword, exception);
            return List.of();
        }
    }


    public Report getById(UUID reportId) {
        return repository.findById(reportId).orElseThrow(() -> new ReportDoesNotExistException());
    }

    public List<Report> getDraftReportsForReminder(ReportStatus status, LocalDateTime remindAfter) {
        return repository.findAllByReportStatusAndCreatedModifiedAt_UpdatedAtBeforeAndDraftReminderSentAtIsNull(status, remindAfter);
    }

    public void saveReport(Report report) {
        repository.save(report);
        log.info("[report-update] Report with id {} was updated successfully", report.getId());
    }
}
