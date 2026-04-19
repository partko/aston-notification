package com.example.notificationservice.controller;

import com.example.notificationservice.dto.SendEmailRequest;
import com.example.notificationservice.service.EmailService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST-контроллер для отправки email уведомлений.
 */
@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final EmailService emailService;

    public NotificationController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send")
    @ResponseStatus(HttpStatus.OK)
    public void send(@Valid @RequestBody SendEmailRequest request) {
        emailService.sendEmail(request.getEmail(), request.getSubject(), request.getText());
    }
}