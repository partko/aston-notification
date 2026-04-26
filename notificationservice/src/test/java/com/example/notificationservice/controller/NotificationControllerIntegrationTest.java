package com.example.notificationservice.controller;

import com.example.notificationservice.dto.SendEmailRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Интеграционный тест ручной отправки email через REST API.
 */
@SpringBootTest
@AutoConfigureMockMvc
class NotificationControllerIntegrationTest {

    private static final int SMTP_PORT = 3025;

    private static final GreenMail greenMail =
            new GreenMail(new ServerSetup(SMTP_PORT, "localhost", ServerSetup.PROTOCOL_SMTP));

    @BeforeAll
    static void startMailServer() {
        greenMail.start();
        greenMail.setUser("test@example.com", "test");
    }

    @AfterAll
    static void stopMailServer() {
        greenMail.stop();
    }

    @DynamicPropertySource
    static void configureMail(DynamicPropertyRegistry registry) {
        registry.add("spring.mail.host", () -> "localhost");
        registry.add("spring.mail.port", () -> SMTP_PORT);
        registry.add("spring.mail.username", () -> "test@example.com");
        registry.add("spring.mail.password", () -> "test");
        registry.add("spring.mail.properties.mail.smtp.auth", () -> "true");
        registry.add("spring.mail.properties.mail.smtp.starttls.enable", () -> "false");
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void send_shouldSendEmail() throws Exception {
        SendEmailRequest request = new SendEmailRequest(
                "test@gmail.com",
                "Test",
                "Test message"
        );

        mockMvc.perform(post("/api/notifications/send")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        MimeMessage[] messages = greenMail.getReceivedMessages();
        assertEquals(1, messages.length);
        assertEquals("Test", messages[0].getSubject());
        assertEquals("test@gmail.com", messages[0].getAllRecipients()[0].toString());
    }
}