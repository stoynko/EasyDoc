package com.github.stoynko.easydoc.services;

import com.github.stoynko.easydoc.models.Report;
import com.github.stoynko.easydoc.repositories.ReportRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportService {

    private final ReportRepository repository;

    @Autowired
    public ReportService(ReportRepository repository) {
        this.repository = repository;
    }

    public List<Report> getDoctorReports(UUID doctorId) {
        return null;
    }
}
