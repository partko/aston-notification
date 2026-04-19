package com.example.userservice.kafka.producer.handler;

import com.example.userservice.kafka.producer.config.KafkaProducerProperties;
import com.example.userservice.kafka.producer.model.OperationType;
import com.example.userservice.kafka.producer.model.UserEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Обработчик отправки Kafka-событий о пользователях.
 */
@Component
public class UserEventProducer {

    private final KafkaTemplate<String, UserEvent> kafkaTemplate;
    private final KafkaProducerProperties properties;

    public UserEventProducer(KafkaTemplate<String, UserEvent> kafkaTemplate,
                             KafkaProducerProperties properties) {
        this.kafkaTemplate = kafkaTemplate;
        this.properties = properties;
    }

    public void sendUserCreated(String email) {
        kafkaTemplate.send(
                properties.getUserEventsTopic(),
                email,
                new UserEvent(OperationType.CREATE, email)
        );
    }

    public void sendUserDeleted(String email) {
        kafkaTemplate.send(
                properties.getUserEventsTopic(),
                email,
                new UserEvent(OperationType.DELETE, email)
        );
    }
}