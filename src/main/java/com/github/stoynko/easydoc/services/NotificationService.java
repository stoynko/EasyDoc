package com.github.stoynko.easydoc.services;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final JavaMailSender mailSender;

    public NotificationService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendVerificationEmail(String to, String verificationLink) {
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

        sendHtmlEmail(to, subject, htmlBody);
    }

    private void sendHtmlEmail(String to, String subject, String htmlBody) {
        try {
            var mimeMessage = mailSender.createMimeMessage();
            var helper = new MimeMessageHelper(mimeMessage, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true); // true => HTML
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            // TODO: log / handle
        }
    }

}