package com.example.notificationservice.kafka.consumer.config;

import com.example.notificationservice.kafka.consumer.model.UserEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

/**
 * Конфигурация Kafka consumer.
 */
@Configuration
@EnableConfigurationProperties(KafkaConsumerProperties.class)
public class KafkaConsumerConfig {

    @Bean
    public Map<String, Object> consumerConfigs(KafkaConsumerProperties properties) {
        Map<String, Object> configs = new HashMap<>();
        configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getBootstrapServers());
        configs.put(ConsumerConfig.GROUP_ID_CONFIG, properties.getGroupId());
        configs.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, properties.getAutoOffsetReset());
        configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return configs;
    }

    @Bean
    public ConsumerFactory<String, UserEvent> consumerFactory(
            Map<String, Object> consumerConfigs
    ) {
        JsonDeserializer<UserEvent> jsonDeserializer = new JsonDeserializer<>(UserEvent.class);
        jsonDeserializer.addTrustedPackages("*");
        jsonDeserializer.setUseTypeHeaders(false);

        return new DefaultKafkaConsumerFactory<>(
                consumerConfigs,
                new StringDeserializer(),
                jsonDeserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, UserEvent> kafkaListenerContainerFactory(
            ConsumerFactory<String, UserEvent> consumerFactory
    ) {
        ConcurrentKafkaListenerContainerFactory<String, UserEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }
}