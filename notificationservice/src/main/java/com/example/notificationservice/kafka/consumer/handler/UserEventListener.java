package com.example.notificationservice.kafka.consumer.handler;

import com.example.notificationservice.kafka.consumer.model.UserEvent;
import com.example.notificationservice.service.UserEventHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Kafka listener для обработки событий пользователей.
 */
@Component
public class UserEventListener {

    private final UserEventHandler handler;

    public UserEventListener(UserEventHandler handler) {
        this.handler = handler;
    }

    @KafkaListener(
            topics = "${app.kafka.consumer.user-events-topic}",
            groupId = "${app.kafka.consumer.group-id}"
    )
    public void listen(UserEvent event) {
        handler.handle(event);
    }
}