package com.segwarez.observability.database.application.rest;

import com.segwarez.observability.database.infrastructure.repository.JPAMessageRepository;
import com.segwarez.observability.database.infrastructure.repository.entity.MessageEntity;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ReceiverController {
    private final JPAMessageRepository messageRepository;

    @GetMapping(value = "/receive")
    public ResponseEntity<List<UUID>> receive() {
        log.info("Fetching all message IDs from database");
        var messageIds = messageRepository.findAll().stream().map(MessageEntity::getId).toList();
        log.info("Mapping message IDs: {}", messageIds);
        var sortedIds = sort(messageIds);
        log.info("Returning sorted IDs: {}", sortedIds);
        return ResponseEntity.ok(sortedIds);
    }

    @WithSpan("db.sort")
    public List<UUID> sort(List<UUID> uuids) {
        log.info("Sorting list of IDs");
        Collections.sort(new ArrayList<>(uuids));
        return uuids;
    }
}