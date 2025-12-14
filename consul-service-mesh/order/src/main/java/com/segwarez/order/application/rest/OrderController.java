package com.segwarez.order.application.rest;

import com.segwarez.order.infrastructure.service.DeliveryService;
import com.segwarez.order.infrastructure.service.WarehouseService;
import com.segwarez.order.infrastructure.service.BillingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderController {
    private final WarehouseService warehouseService;
    private final BillingService billingService;
    private final DeliveryService deliveryService;

    @GetMapping(value = "/order", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> order() {
        if (!warehouseService.reserve()) return ResponseEntity.badRequest().build();
        billingService.pay();
        return ResponseEntity.ok(deliveryService.deliver());
    }
}
