package com.example.userservice.kafka.producer.config;

import com.example.userservice.kafka.producer.model.UserEvent;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

/**
 * Конфигурация Kafka producer.
 */
@Configuration
@EnableConfigurationProperties(KafkaProducerProperties.class)
public class KafkaProducerConfig {

    @Bean
    public Map<String, Object> producerConfigs(KafkaProducerProperties properties) {
        Map<String, Object> configs = new HashMap<>();
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getBootstrapServers());
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        configs.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
        return configs;
    }

    @Bean
    public ProducerFactory<String, UserEvent> producerFactory(
            Map<String, Object> producerConfigs
    ) {
        return new DefaultKafkaProducerFactory<>(producerConfigs);
    }

    @Bean
    public KafkaTemplate<String, UserEvent> kafkaTemplate(
            ProducerFactory<String, UserEvent> producerFactory
    ) {
        return new KafkaTemplate<>(producerFactory);
    }
}