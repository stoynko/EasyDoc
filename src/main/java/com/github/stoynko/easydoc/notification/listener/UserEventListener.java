package com.github.stoynko.easydoc.notification.listener;

import com.github.stoynko.easydoc.user.event.RegistrationCompletedEvent;
import com.github.stoynko.easydoc.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class UserEventListener {

    public static final String baseUrl = "localhost:8080";

    private final NotificationService notificationService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleRegistrationCompleted(RegistrationCompletedEvent event) {
        String verificationLink = baseUrl + "/auth/verify-email?token=" + event.token();
        notificationService.sendVerificationEmail(event.emailAddress(), verificationLink);
    }

}
