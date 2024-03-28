package com.user.user.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.user.user.domain.email.EmailDTO;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(EmailDTO email) {
        var message = new SimpleMailMessage();
        message.setTo(email.to());
        message.setSubject(email.subject());
        message.setText(email.body());

        mailSender.send(message);
    }

    public void sendHtmlEmail(EmailDTO email) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            message.setRecipients(MimeMessage.RecipientType.TO, email.to());
            message.setSubject(email.subject());

            String htmlContent = email.body();

            message.setContent(htmlContent, "text/html; charset=utf-8");

            mailSender.send(message);
        } catch (Exception e) {
            throw e;
        }
    }
}