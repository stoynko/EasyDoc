package com.github.stoynko.easydoc.services;

import com.github.stoynko.easydoc.exceptions.ReportDoesNotExistException;
import com.github.stoynko.easydoc.models.Report;
import com.github.stoynko.easydoc.repositories.ReportRepository;
import com.github.stoynko.easydoc.web.dto.request.CreateMedicalReportRequest;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository repository;

    public List<Report> getDoctorReports(UUID doctorId) {
        return null;
    }

    public void createMedicalReport(UUID appointmentId, CreateMedicalReportRequest request) {
        System.out.println();
    }

    public void editMedicalReport(UUID appointmentId, CreateMedicalReportRequest request) {
        System.out.println();
    }

    public Report getById(UUID reportId) {
        return repository.findById(reportId).orElseThrow(() -> new ReportDoesNotExistException());
    }
}
