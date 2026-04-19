package com.example.userservice.kafka.handler;

import com.example.userservice.kafka.producer.model.OperationType;
import com.example.userservice.kafka.producer.model.UserEvent;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Интеграционные тесты Kafka producer в user-service.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class UserEventProducerIntegrationTest {

    private static final String TOPIC = "user-events";

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17")
            .withDatabaseName("user_service_test_db")
            .withUsername("postgres")
            .withPassword("postgres");

    @Container
    static KafkaContainer kafka = new KafkaContainer(
            DockerImageName.parse("apache/kafka:3.7.0")
    );

    @DynamicPropertySource
    static void configure(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        registry.add("app.kafka.producer.bootstrap-servers", kafka::getBootstrapServers);
        registry.add("app.kafka.producer.user-events-topic", () -> TOPIC);
    }

    @Autowired
    private MockMvc mockMvc;

    private Consumer<String, UserEvent> consumer;

    @BeforeEach
    void setUp() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "user-service-test-consumer-" + UUID.randomUUID());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        JsonDeserializer<UserEvent> jsonDeserializer = new JsonDeserializer<>(UserEvent.class);
        jsonDeserializer.addTrustedPackages("*");
        jsonDeserializer.setUseTypeHeaders(false);

        consumer = new DefaultKafkaConsumerFactory<>(
                props,
                new StringDeserializer(),
                jsonDeserializer
        ).createConsumer();

        consumer.subscribe(java.util.List.of(TOPIC));
        consumer.poll(Duration.ofMillis(500));
    }

    @AfterEach
    void tearDown() {
        if (consumer != null) {
            consumer.close();
        }
    }

    @Test
    @DisplayName("POST /api/users должен публиковать CREATE событие в Kafka")
    void createUser_shouldPublishCreateEventToKafka() throws Exception {
        String requestBody = """
                {
                  "name": "Vladimir",
                  "email": "vvv@gmail.com",
                  "age": 20
                }
                """;

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated());
        ConsumerRecord<String, UserEvent> record =
                waitForMatchingRecord("vvv@gmail.com", OperationType.CREATE);

        assertNotNull(record);
        assertEquals("vvv@gmail.com", record.key());
        assertNotNull(record.value());
        assertEquals(OperationType.CREATE, record.value().getOperation());
        assertEquals("vvv@gmail.com", record.value().getEmail());
    }

    @Test
    @DisplayName("DELETE /api/users/{id} должен публиковать DELETE событие в Kafka")
    void deleteUser_shouldPublishDeleteEventToKafka() throws Exception {
        String createBody = """
                {
                  "name": "Vladimir",
                  "email": "vvv@gmail.com",
                  "age": 20
                }
                """;
        String response = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createBody))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        com.fasterxml.jackson.databind.JsonNode json =
                new com.fasterxml.jackson.databind.ObjectMapper().readTree(response);
        long userId = json.get("id").asLong();
        waitForMatchingRecord("vvv@gmail.com", OperationType.CREATE);

        mockMvc.perform(delete("/api/users/{id}", userId))
                .andExpect(status().isNoContent());
        ConsumerRecord<String, UserEvent> record =
                waitForMatchingRecord("vvv@gmail.com", OperationType.DELETE);

        assertNotNull(record);
        assertEquals("vvv@gmail.com", record.key());
        assertNotNull(record.value());
        assertEquals(OperationType.DELETE, record.value().getOperation());
        assertEquals("vvv@gmail.com", record.value().getEmail());
    }

    /**
     * Ждёт Kafka-сообщение с нужным email и типом операции.
     */
    private ConsumerRecord<String, UserEvent> waitForMatchingRecord(String email, OperationType operation) {
        long deadline = System.currentTimeMillis() + Duration.ofSeconds(10).toMillis();

        while (System.currentTimeMillis() < deadline) {
            var records = KafkaTestUtils.getRecords(consumer, Duration.ofMillis(500));

            for (ConsumerRecord<String, UserEvent> record : records.records(TOPIC)) {
                if (record.value() != null
                        && operation == record.value().getOperation()
                        && email.equals(record.value().getEmail())) {
                    return record;
                }
            }
        }

        fail("Не найдено Kafka-сообщение с operation=" + operation + " и email=" + email);
        return null;
    }
}