package com.user.user.services;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.user.user.domains.email.EmailDTO;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(EmailDTO email) {
        var message = new SimpleMailMessage();
        message.setFrom("noreply@email.com");
        message.setTo(email.to());
        message.setSubject(email.subject());
        message.setText(email.body());

        mailSender.send(message);
    }
}