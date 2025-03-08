package com.segwarez.kafkastreamspageviews.serdes;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
public class SetDeserializer<T> implements Deserializer<Set<T>> {
    private final Deserializer<T> valueDeserializer;

    public SetDeserializer(Deserializer<T> valueDeserializer) {
        this.valueDeserializer = valueDeserializer;
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        valueDeserializer.configure(configs, isKey);
    }

    @Override
    public Set<T> deserialize(String topic, byte[] data) {
        if (data == null) {
            return Collections.emptySet();
        }
        Set<T> set = new HashSet<>();
        try (var dataInputStream = new DataInputStream(new ByteArrayInputStream(data))) {
            int size = dataInputStream.readInt();
            for (int i = 0; i < size; i++) {
                byte[] valueBytes = new byte[dataInputStream.readInt()];
                dataInputStream.read(valueBytes);
                set.add(valueDeserializer.deserialize(topic, valueBytes));
            }
        } catch (IOException e) {
            log.error("Set deserialization error: {}", e.getMessage());
            throw new RuntimeException(e);
        }
        return set;
    }

    @Override
    public void close() {
        valueDeserializer.close();
    }
}
