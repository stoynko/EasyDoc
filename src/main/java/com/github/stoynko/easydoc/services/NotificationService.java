package com.github.stoynko.easydoc.services;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final JavaMailSender mailSender;

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

    public void sendAppointmentCompletedEmail(String recipient, String reportLink) {
        String subject = "Appointment Completed";

        String htmlBody = """
            <p>Your appointment with doctor "Placeholder Name" on "Placeholder Date" has been marked as completed.</p>
            <p>
              <a href="%s"
                 style="display:inline-block;padding:10px 18px;background:#2563eb;
                        color:#ffffff;text-decoration:none;border-radius:4px;">
                Medical Report
              </a>
            </p>
            """.formatted(reportLink);

        sendHtmlEmail(recipient, subject, htmlBody);
    }

    private void sendHtmlEmail(String to, String subject, String htmlBody) {
        try {
            var mimeMessage = mailSender.createMimeMessage();
            var helper = new MimeMessageHelper(mimeMessage, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            // TODO: log / handle
        }
    }

}