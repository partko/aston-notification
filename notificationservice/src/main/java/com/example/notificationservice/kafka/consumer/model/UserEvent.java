package com.example.notificationservice.kafka.consumer.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Событие пользователя, получаемое из Kafka.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserEvent {

    private OperationType operation;
    private String email;

}