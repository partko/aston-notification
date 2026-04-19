package com.example.notificationservice.service.impl;

import com.example.notificationservice.service.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Реализация сервиса отправки почты.
 */
@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final String from;

    public EmailServiceImpl(JavaMailSender mailSender, @Value("${spring.mail.username}") String from) {
        this.mailSender = mailSender;
        this.from = from;
    }

    @Override
    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    @Override
    public void sendUserCreatedEmail(String to) {
        sendEmail(
                to,
                "Аккаунт создан",
                "Ваш аккаунт был успешно создан"
        );
    }

    @Override
    public void sendUserDeletedEmail(String to) {
        sendEmail(
                to,
                "Аккаунт удалён",
                "Ваш аккаунт был успешно удалён"
        );
    }
}