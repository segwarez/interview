package com.segwarez.kafkastreamspageviews.serdes;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serializer;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@Slf4j
public class SetSerializer<T> implements Serializer<Set<T>> {
    private final Serializer<T> valueSerializer;

    public SetSerializer(Serializer<T> valueSerializer) {
        this.valueSerializer = valueSerializer;
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        valueSerializer.configure(configs, isKey);
    }

    @Override
    public byte[] serialize(String topic, Set<T> data) {
        var size = data.size();
        var byteArrayOutputStream = new ByteArrayOutputStream();
        Iterator<T> iterator = data.iterator();
        try (var dataOutputStream = new DataOutputStream(byteArrayOutputStream);) {
            dataOutputStream.writeInt(size);
            while (iterator.hasNext()) {
                byte[] bytes = valueSerializer.serialize(topic, iterator.next());
                dataOutputStream.writeInt(bytes.length);
                dataOutputStream.write(bytes);
            }
        } catch (IOException e) {
            log.error("Set serialization error: {}", e.getMessage());
            throw new RuntimeException(e);
        }
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public void close() {
        valueSerializer.close();
    }
}
