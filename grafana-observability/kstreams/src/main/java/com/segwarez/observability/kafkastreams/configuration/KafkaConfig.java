package com.segwarez.observability.kafkastreams.configuration;

import org.apache.kafka.common.serialization.Serdes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;

import java.util.HashMap;
import java.util.Map;

import static org.apache.kafka.streams.StreamsConfig.*;

@EnableKafka
@EnableKafkaStreams
@Configuration
public class KafkaConfig {
    public static final String INPUT_TOPIC = "com.segwarez.observability.web";
    public static final String OUTPUT_TOPIC = "com.segwarez.observability.database";

    @Value(value = "${spring.application.name}")
    private String applicationName;
    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    KafkaStreamsConfiguration kafkaStreamsConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put(APPLICATION_ID_CONFIG, applicationName);
        props.put(BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        return new KafkaStreamsConfiguration(props);
    }
}