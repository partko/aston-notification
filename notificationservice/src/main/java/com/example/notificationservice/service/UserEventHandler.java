package com.example.notificationservice.service;

import com.example.notificationservice.kafka.consumer.model.UserEvent;

/**
 * Обрабатывает события пользователей.
 */
public interface UserEventHandler {

    void handle(UserEvent event);
}