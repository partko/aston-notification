package com.example.notificationservice.service.impl;

import com.example.notificationservice.kafka.consumer.model.OperationType;
import com.example.notificationservice.kafka.consumer.model.UserEvent;
import com.example.notificationservice.service.EmailService;
import com.example.notificationservice.service.UserEventHandler;
import org.springframework.stereotype.Service;

/**
 * Реализация обработки событий пользователей.
 */
@Service
public class UserEventHandlerImpl implements UserEventHandler {

    private final EmailService emailService;

    public UserEventHandlerImpl(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public void handle(UserEvent event) {
        if (event.getOperation() == OperationType.CREATE) {
            emailService.sendUserCreatedEmail(event.getEmail());
        } else if (event.getOperation() == OperationType.DELETE) {
            emailService.sendUserDeletedEmail(event.getEmail());
        }
    }
}