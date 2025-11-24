package com.github.stoynko.easydoc.listeners;

import com.github.stoynko.easydoc.events.RegistrationCompletedEvent;
import com.github.stoynko.easydoc.services.NotificationService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class UserEventListener {

    public static final String baseUrl = "localhost:8080";

    private final NotificationService notificationService;

    public UserEventListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleRegistrationCompleted(RegistrationCompletedEvent event) {
        String verificationLink = baseUrl + "/auth/verify-email?token=" + event.token();
        notificationService.sendVerificationEmail(event.emailAddress(), verificationLink);
    }
}
