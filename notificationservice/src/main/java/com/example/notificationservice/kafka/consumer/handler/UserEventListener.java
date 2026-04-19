package com.example.notificationservice.kafka.consumer.handler;

import com.example.notificationservice.kafka.consumer.model.OperationType;
import com.example.notificationservice.kafka.consumer.model.UserEvent;
import com.example.notificationservice.service.EmailService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Kafka listener для обработки событий пользователей.
 */
@Component
public class UserEventListener {

    private final EmailService emailService;

    public UserEventListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @KafkaListener(
            topics = "${app.kafka.consumer.user-events-topic}",
            groupId = "${app.kafka.consumer.group-id}"
    )

    public void listen(UserEvent event) {
        if (event.getOperation() == OperationType.CREATE) {
            emailService.sendUserCreatedEmail(event.getEmail());
        } else if (event.getOperation() == OperationType.DELETE) {
            emailService.sendUserDeletedEmail(event.getEmail());
        }
    }
}