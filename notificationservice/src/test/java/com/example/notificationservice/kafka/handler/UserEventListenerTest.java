package com.example.notificationservice.kafka.handler;

import com.example.notificationservice.kafka.consumer.handler.UserEventListener;
import com.example.notificationservice.kafka.consumer.model.OperationType;
import com.example.notificationservice.kafka.consumer.model.UserEvent;
import com.example.notificationservice.service.EmailService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * Unit-тесты listener-а Kafka-событий.
 */
class UserEventListenerTest {

    private final EmailService emailService = Mockito.mock(EmailService.class);
    private final UserEventListener listener = new UserEventListener(emailService);

    @Test
    @DisplayName("listener должен вызывать sendUserCreatedEmail для CREATE события")
    void listen_shouldCallSendUserCreatedEmail() {
        UserEvent event = new UserEvent(OperationType.CREATE, "user@gmail.com");

        listener.listen(event);

        verify(emailService).sendUserCreatedEmail("user@gmail.com");
        verifyNoMoreInteractions(emailService);
    }

    @Test
    @DisplayName("listener должен вызывать sendUserDeletedEmail для DELETE события")
    void listen_shouldCallSendUserDeletedEmail() {
        UserEvent event = new UserEvent(OperationType.DELETE, "deleted@gmail.com");

        listener.listen(event);

        verify(emailService).sendUserDeletedEmail("deleted@gmail.com");
        verifyNoMoreInteractions(emailService);
    }
}