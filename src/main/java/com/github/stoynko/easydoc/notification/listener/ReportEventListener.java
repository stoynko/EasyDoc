package com.github.stoynko.easydoc.notification.listener;

import com.github.stoynko.easydoc.notification.service.NotificationService;
import com.github.stoynko.easydoc.report.event.DraftReminderEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReportEventListener {

    private final NotificationService notificationService;

    @Async
    @EventListener
    public void handleRegistrationCompleted(DraftReminderEvent event) {
        notificationService.sendReportReminderEmail(event.getRecipient(), event.getPatientName(), event.getAtDate());
    }
}
