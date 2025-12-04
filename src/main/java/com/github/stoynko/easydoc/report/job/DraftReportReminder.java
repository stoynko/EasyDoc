package com.github.stoynko.easydoc.report.job;

import com.github.stoynko.easydoc.report.model.Report;
import com.github.stoynko.easydoc.report.service.ReportService;
import com.github.stoynko.easydoc.shared.mapper.EventMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.github.stoynko.easydoc.report.model.ReportStatus.DRAFT;

@Slf4j
@Service
@RequiredArgsConstructor
public class DraftReportReminder {

    private final ReportService reportService;
    private final ApplicationEventPublisher eventPublisher;

    @Scheduled(fixedRate = 24 * 60 * 60 * 1000L, initialDelay = 10_000L)
    public void remindOnDraftReports() {

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime remindAfter = LocalDateTime.now().minusNanos(1);

        List<Report> drafts = reportService.getDraftReportsForReminder(DRAFT, remindAfter);

        for (Report report : drafts) {
            try {
                report.setDraftReminderSentAt(now);
                reportService.saveReport(report);
                eventPublisher.publishEvent(EventMapper.toDraftReminderEvent(report));
                log.info("[report-reminder-sent] Reminder was sent for report with id %s".formatted(report.getId()));
            } catch (Exception exception) {
                log.error("[report-reminder-failed] Reminder for report with id %s could not be sent due to %s".formatted(report.getId()), exception.getMessage());
            }
        }
    }
}

