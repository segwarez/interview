package com.segwarez.observability.web.application.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static com.segwarez.observability.web.infrastructure.configuration.KafkaConfig.WEB_TOPIC;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SenderController {
    private final KafkaTemplate<String, String> kafkaTemplate;

    @GetMapping(value = "/send")
    public ResponseEntity<String> send() {
        log.info("Sending message");
        var key = UUID.randomUUID().toString();
        kafkaTemplate.send(WEB_TOPIC, key, "message");
        log.info("Message sent with key: {}", key);
        return ResponseEntity.ok(key);
    }
}