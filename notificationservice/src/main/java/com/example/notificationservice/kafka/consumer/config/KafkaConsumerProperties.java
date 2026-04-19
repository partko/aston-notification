package com.example.notificationservice.kafka.consumer.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Класс с настройками Kafka consumer.
 */
@ConfigurationProperties(prefix = "app.kafka.consumer")
public class KafkaConsumerProperties {

    private String bootstrapServers;
    private String groupId;
    private String userEventsTopic;
    private String autoOffsetReset = "earliest";

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public void setBootstrapServers(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getUserEventsTopic() {
        return userEventsTopic;
    }

    public void setUserEventsTopic(String userEventsTopic) {
        this.userEventsTopic = userEventsTopic;
    }

    public String getAutoOffsetReset() {
        return autoOffsetReset;
    }

    public void setAutoOffsetReset(String autoOffsetReset) {
        this.autoOffsetReset = autoOffsetReset;
    }
}