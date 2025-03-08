package com.segwarez.kafkastreamspageviews.serdes;

import com.segwarez.kafkastreamspageviews.model.PageViewEvent;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class SerdesFactory {
    public Serde<String> stringSerde() {
        return Serdes.String();
    }

    public Serde<PageViewEvent> pageViewSerde() {
        return new JsonSerde<>(PageViewEvent.class);
    }

    public Serde<Set<String>> setStringSerde() {
        var setSerializer = new SetSerializer<>(stringSerde().serializer());
        var setDeserializer = new SetDeserializer<>(stringSerde().deserializer());
        return Serdes.serdeFrom(setSerializer, setDeserializer);
    }
}
