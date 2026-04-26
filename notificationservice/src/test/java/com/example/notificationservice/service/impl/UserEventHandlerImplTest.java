package com.example.notificationservice.service.impl;

import com.example.notificationservice.kafka.consumer.model.OperationType;
import com.example.notificationservice.kafka.consumer.model.UserEvent;
import com.example.notificationservice.service.EmailService;
import com.example.notificationservice.service.UserEventHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * Unit-тест обработчика событий пользователей.
 */
class UserEventHandlerImplTest {

    private EmailService emailService;
    private UserEventHandler handler;

    @BeforeEach
    void setUp() {
        emailService = mock(EmailService.class);
        handler = new UserEventHandlerImpl(emailService);
    }

    @Test
    @DisplayName("handler должен вызывать sendUserCreatedEmail для CREATE события")
    void handle_shouldSendCreatedEmail() {
        UserEvent event = new UserEvent(OperationType.CREATE, "user@gmail.com");

        handler.handle(event);

        verify(emailService).sendUserCreatedEmail("user@gmail.com");
        verifyNoMoreInteractions(emailService);
    }

    @Test
    @DisplayName("handler должен вызывать sendUserDeletedEmail для DELETE события")
    void handle_shouldSendDeletedEmail_whenOperationIsDelete() {
        UserEvent event = new UserEvent(OperationType.DELETE, "user@gmail.com");

        handler.handle(event);

        verify(emailService).sendUserDeletedEmail("user@gmail.com");
        verifyNoMoreInteractions(emailService);
    }
}