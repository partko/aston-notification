package com.example.userservice.kafka.producer.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Настройки Kafka producer для user-service.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "app.kafka.producer")
public class KafkaProducerProperties {

    private String bootstrapServers;
    private String userEventsTopic;

}