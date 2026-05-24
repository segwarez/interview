package com.segwarez.delivery.application;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class DeliveryController {
    @Value("${VERSION:v1}")
    private String version;

    @GetMapping(value = "/deliver", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deliver() {
        log.info("Delivering version {}", version);
        return ResponseEntity.ok(version);
    }
}