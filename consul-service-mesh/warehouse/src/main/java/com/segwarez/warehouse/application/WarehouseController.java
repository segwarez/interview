package com.segwarez.warehouse.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ThreadLocalRandom;

@RestController
public class WarehouseController {
    private static final Logger log = LoggerFactory.getLogger(WarehouseController.class);

    @GetMapping(value = "/reserve", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> reserve() {
        log.info("Reserving stock");
        if (ThreadLocalRandom.current().nextBoolean()) {
            log.info("Stock reserved successfully");
            return ResponseEntity.ok().build();
        }
        log.info("Not enough stock");
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> update() {
        log.info("Updating stock");
        return ResponseEntity.ok().build();
    }
}