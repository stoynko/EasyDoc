package com.github.stoynko.easydoc.services;

import com.github.stoynko.easydoc.client.DiagnosisClient;
import com.github.stoynko.easydoc.exceptions.ReportDoesNotExistException;
import com.github.stoynko.easydoc.models.Appointment;
import com.github.stoynko.easydoc.models.Report;
import com.github.stoynko.easydoc.repositories.ReportRepository;
import com.github.stoynko.easydoc.web.dto.request.MedicalReportRequest;

import com.github.stoynko.easydoc.web.dto.response.DiagnosisOptionResponse;
import com.github.stoynko.easydoc.web.dto.response.Icd10SearchResponse;
import com.github.stoynko.easydoc.web.mappers.EntityMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReportService {

    public static final int MAX_DIAGNOSE_ENTRIES = 50;

    private final ReportRepository repository;
    private final AppointmentService appointmentService;
    private final DiagnosisClient diagnosisClient;

    @Transactional
    public void createMedicalReport(Appointment appointment, MedicalReportRequest request) {
        Report report = EntityMapper.ToReportEntity(request, appointment);
        repository.save(report);

        appointment.setReport(report);
        appointmentService.saveAppointment(appointment);
    }

    public void editMedicalReport(Appointment appointment, MedicalReportRequest request) {
        //TODO:
        System.out.println("EDIT");
    }

    public List<DiagnosisOptionResponse> findDiagnosisByKeyword(String keyword) {

        List<DiagnosisOptionResponse> diagnosisOptions = null;
        try {
            Icd10SearchResponse response = diagnosisClient.searchIcd10("code,name", keyword, MAX_DIAGNOSE_ENTRIES);
            diagnosisOptions = response.rows().stream()
                    .map(row -> new DiagnosisOptionResponse(row.get(0) + " " + row.get(1)))
                    .collect(Collectors.toList());
        } catch (FeignException exception) {
            diagnosisOptions = new ArrayList<DiagnosisOptionResponse>();
        }

        return diagnosisOptions;
    }

    public List<Report> getDoctorReports(UUID doctorId) {
        return null;
    }

    public Report getById(UUID reportId) {
        return repository.findById(reportId).orElseThrow(() -> new ReportDoesNotExistException());
    }
}
