package com.segwarez.kafkastreamspageviews.controller;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

import static com.segwarez.kafkastreamspageviews.configuration.KafkaConfig.PAGE_VIEW_EVENT_STORE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/statistics/pages")
public class PageStatisticsController {
    private final StreamsBuilderFactoryBean factoryBean;

    @GetMapping("/{id}/views")
    public ResponseEntity<Integer> getNumberOfUniqueUsers(@PathVariable("id") String pageId) {
        KafkaStreams kafkaStreams = factoryBean.getKafkaStreams();
        ReadOnlyKeyValueStore<String, Set<String>> page = kafkaStreams.store(
                StoreQueryParameters.fromNameAndType(PAGE_VIEW_EVENT_STORE, QueryableStoreTypes.keyValueStore()));

        var views = page.get(pageId);
        if (views != null) return ResponseEntity.ok(views.size());
        return ResponseEntity.notFound().build();
    }
}
