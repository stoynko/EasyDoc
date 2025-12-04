package com.github.stoynko.easydoc.notification.service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final JavaMailSender mailSender;

    @Value("${EMAIL_OUTLOOK}")
    private String fromAddress;

    private void sendHtmlEmail(String recipient, String subject, String htmlBody) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "UTF-8");

            helper.setFrom(fromAddress);
            helper.setTo(recipient);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);

            mailSender.send(mimeMessage);
        } catch (Exception e) {
            log.error("[email-delivery-failed] Failed to send email to {} with subject {}: {}", recipient, subject, e.getMessage(), e);
        }
    }

    public void sendVerificationEmail(String recipient, String verificationLink) {
        String subject = "Welcome to EasyDoc account";

        String htmlBody = """
            <p>Welcome to EasyDoc!</p>
            <p>To activate your account, click the button below:</p>
            <p>
              <a href="%s"
                 style="display:inline-block;padding:10px 18px;background:#2563eb;
                        color:#ffffff;text-decoration:none;border-radius:4px;">
                Verify email
              </a>
            </p>
            <p>If the button doesn't work, copy and paste this link into your browser:</p>
            <p><a href="%s">%s</a></p>
            """.formatted(verificationLink, verificationLink, verificationLink);

        sendHtmlEmail(recipient, subject, htmlBody);
    }

    public void sendReportReminderEmail(String recipient, String patientName, String atDate) {
        String subject = "Draft Medical Report Reminder";

        String htmlBody = """
        <p>Please note that you have a medical report for %s in <strong>draft</strong> that took place on  <strong>%s</strong>.</p>
        <p>Please review and finalize the report when possible.</p>
        """.formatted(patientName, atDate);

        sendHtmlEmail(recipient, subject, htmlBody);
    }
}