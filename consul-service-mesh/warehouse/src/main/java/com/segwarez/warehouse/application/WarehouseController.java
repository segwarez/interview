package com.segwarez.warehouse.application;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@RestController
public class WarehouseController {
    @GetMapping(value = "/reserve", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> reserve() {
        log.info("Reserving stock");
        if (ThreadLocalRandom.current().nextBoolean()) {
            log.info("Stock reserved successfully");
            return ResponseEntity.ok().build();
        }
        log.info("Not enough stock");
        return ResponseEntity.internalServerError().build();
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> update() {
        log.info("Updating stock");
        return ResponseEntity.ok().build();
    }
}