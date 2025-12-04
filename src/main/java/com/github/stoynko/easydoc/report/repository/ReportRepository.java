package com.github.stoynko.easydoc.report.repository;

import com.github.stoynko.easydoc.report.model.Report;
import com.github.stoynko.easydoc.report.model.ReportStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ReportRepository extends JpaRepository<Report, UUID> {

    List<Report> findAllByReportStatusAndCreatedModifiedAt_UpdatedAtBeforeAndDraftReminderSentAtIsNull
            (ReportStatus status, LocalDateTime remindAfter);
}
