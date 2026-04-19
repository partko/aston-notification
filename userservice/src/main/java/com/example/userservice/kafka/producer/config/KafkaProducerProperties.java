package com.example.userservice.kafka.producer.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Настройки Kafka producer для user-service.
 */
@ConfigurationProperties(prefix = "app.kafka.producer")
public class KafkaProducerProperties {

    private String bootstrapServers;
    private String userEventsTopic;

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public void setBootstrapServers(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    public String getUserEventsTopic() {
        return userEventsTopic;
    }

    public void setUserEventsTopic(String userEventsTopic) {
        this.userEventsTopic = userEventsTopic;
    }
}