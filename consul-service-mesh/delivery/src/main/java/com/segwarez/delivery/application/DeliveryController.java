package com.segwarez.delivery.application;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeliveryController {
    @Value("${VERSION:v1}")
    private String version;

    @GetMapping(value = "/deliver", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deliver() {
        return ResponseEntity.ok(version);
    }
}
