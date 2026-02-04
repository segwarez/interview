package com.segwarez.billing.application.rest;

import com.segwarez.billing.infrastructure.external.WarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BillingController {
    private final WarehouseService warehouseService;

    @PostMapping(value = "/pay", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> pay() {
        warehouseService.update();
        return ResponseEntity.ok().build();
    }
}
