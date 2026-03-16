package com.segwarez.observability.kafkastreams.topology;

import com.segwarez.observability.kafkastreams.configuration.KafkaConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.segwarez.observability.kafkastreams.configuration.KafkaConfig.OUTPUT_TOPIC;

@Slf4j
@Component
public class MessageStream {
    private static final Serde<String> STRING_SERDE = Serdes.String();

    @Autowired
    void buildPipeline(StreamsBuilder streamsBuilder) {
        KStream<String, String> messageStream = streamsBuilder
                .stream(KafkaConfig.INPUT_TOPIC, Consumed.with(STRING_SERDE, STRING_SERDE));
        messageStream.peek((key, value) -> log.info("message key={}, value={}", key, value));
        messageStream.to(OUTPUT_TOPIC, Produced.with(STRING_SERDE, STRING_SERDE));
    }
}
