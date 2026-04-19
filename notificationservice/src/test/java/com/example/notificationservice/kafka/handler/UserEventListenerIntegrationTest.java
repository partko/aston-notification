package com.example.notificationservice.kafka.handler;

import com.example.notificationservice.kafka.consumer.model.OperationType;
import com.example.notificationservice.kafka.consumer.model.UserEvent;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.store.FolderException;
import com.icegreen.greenmail.util.ServerSetupTest;
import jakarta.mail.internet.MimeMessage;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.HashMap;
import java.util.Map;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Интеграционные тесты Kafka consumer и отправки email в notification-service.
 */
@SpringBootTest
@Testcontainers
@Import(UserEventListenerIntegrationTest.KafkaTestProducerConfig.class)
class UserEventListenerIntegrationTest {

    private static final String TOPIC = "user-events";

    @Container
    static org.testcontainers.kafka.KafkaContainer kafka = new KafkaContainer(
            DockerImageName.parse("apache/kafka:3.7.0")
    );

    @RegisterExtension
    static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP);

    @DynamicPropertySource
    static void configure(DynamicPropertyRegistry registry) {
        registry.add("app.kafka.consumer.bootstrap-servers", kafka::getBootstrapServers);
        registry.add("app.kafka.consumer.group-id", () -> "notification-test-group");
        registry.add("app.kafka.consumer.user-events-topic", () -> TOPIC);
        registry.add("app.kafka.consumer.auto-offset-reset", () -> "earliest");

        registry.add("spring.mail.host", () -> "localhost");
        registry.add("spring.mail.port", ServerSetupTest.SMTP::getPort);
        registry.add("spring.mail.username", () -> "test@example.com");
        registry.add("spring.mail.password", () -> "test");
    }

    @Autowired
    @Qualifier("testKafkaTemplate")
    private KafkaTemplate<String, UserEvent> kafkaTemplate;

    @BeforeEach
    void setUp() throws FolderException {
        greenMail.setUser("test@example.com", "test");
        greenMail.purgeEmailFromAllMailboxes();
    }

    @Test
    @DisplayName("Kafka CREATE событие должно приводить к отправке письма о создании аккаунта")
    void shouldSendCreateEmailWhenCreateEventReceived() {
        UserEvent event = new UserEvent(OperationType.CREATE, "user@gmail.com");

        kafkaTemplate.send(TOPIC, event.getEmail(), event);
        kafkaTemplate.flush();

        await().untilAsserted(() -> {
            MimeMessage[] messages = greenMail.getReceivedMessages();
            assertEquals(1, messages.length);
            assertEquals("Аккаунт создан", messages[0].getSubject());
            assertEquals("user@gmail.com", messages[0].getAllRecipients()[0].toString());
            Object content = messages[0].getContent();
            assertTrue(content.toString().contains("Ваш аккаунт был успешно создан"));
        });
    }

    @Test
    @DisplayName("Kafka DELETE событие должно приводить к отправке письма об удалении аккаунта")
    void shouldSendDeleteEmailWhenDeleteEventReceived() {
        UserEvent event = new UserEvent(OperationType.DELETE, "deleted@gmail.com");

        kafkaTemplate.send(TOPIC, event.getEmail(), event);
        kafkaTemplate.flush();

        await().untilAsserted(() -> {
            MimeMessage[] messages = greenMail.getReceivedMessages();
            assertEquals(1, messages.length);
            assertEquals("Аккаунт удалён", messages[0].getSubject());
            assertEquals("deleted@gmail.com", messages[0].getAllRecipients()[0].toString());
            Object content = messages[0].getContent();
            assertTrue(content.toString().contains("Ваш аккаунт был успешно удалён"));
        });
    }

    @TestConfiguration
    static class KafkaTestProducerConfig {

        @Bean
        public Map<String, Object> testProducerConfigs() {
            Map<String, Object> config = new HashMap<>();
            config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
            config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
            config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
            config.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
            return config;
        }

        @Bean
        public ProducerFactory<String, UserEvent> testProducerFactory(
                @Qualifier("testProducerConfigs") Map<String, Object> testProducerConfigs
        ) {
            return new DefaultKafkaProducerFactory<>(testProducerConfigs);
        }

        @Bean
        public KafkaTemplate<String, UserEvent> testKafkaTemplate(
                @Qualifier("testProducerFactory") ProducerFactory<String, UserEvent> testProducerFactory
        ) {
            return new KafkaTemplate<>(testProducerFactory);
        }
    }
}