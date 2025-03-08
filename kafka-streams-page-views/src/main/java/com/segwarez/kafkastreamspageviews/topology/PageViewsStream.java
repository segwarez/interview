package com.segwarez.kafkastreamspageviews.topology;

import com.segwarez.kafkastreamspageviews.configuration.KafkaConfig;
import com.segwarez.kafkastreamspageviews.model.PageViewEvent;
import com.segwarez.kafkastreamspageviews.serdes.SerdesFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

import static com.segwarez.kafkastreamspageviews.configuration.KafkaConfig.PAGE_VIEW_EVENT_STORE;

@Slf4j
@Component
@RequiredArgsConstructor
public class PageViewsStream {
    private final SerdesFactory serdesFactory;

    @Autowired
    void buildPipeline(StreamsBuilder streamsBuilder) {
        KStream<String, PageViewEvent> messageStream = streamsBuilder
                .stream(KafkaConfig.PAGE_VIEW_EVENT_TOPIC, Consumed.with(serdesFactory.stringSerde(), serdesFactory.pageViewSerde()));

        messageStream.print(Printed.<String, PageViewEvent>toSysOut().withLabel("message"));

        KTable<String, Set<String>> groupedUserIds = messageStream
                .filter((key, value) -> value != null)
                .groupBy((key, value) -> value.getPageId())
                .aggregate(HashSet::new,
                        this::aggregate,
                        Materialized.<String, Set<String>, KeyValueStore<Bytes, byte[]>>as(PAGE_VIEW_EVENT_STORE)
                                .withKeySerde(serdesFactory.stringSerde())
                                .withValueSerde(serdesFactory.setStringSerde())
                                .withCachingDisabled()
                );
        groupedUserIds.toStream().print(Printed.<String, Set<String>>toSysOut().withLabel("userIds"));
    }

    private Set<String> aggregate(String key, PageViewEvent value, Set<String> uniqueUserIds) {
        uniqueUserIds.add(value.getUserId());
        log.info("Aggregated pageId {} with unique UserIds {}", key, uniqueUserIds);
        return uniqueUserIds;
    }
}
