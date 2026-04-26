package com.example.notificationservice.kafka.consumer.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Класс с настройками Kafka consumer.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "app.kafka.consumer")
public class KafkaConsumerProperties {

    private String bootstrapServers;
    private String groupId;
    private String userEventsTopic;
    private String autoOffsetReset = "earliest";

}