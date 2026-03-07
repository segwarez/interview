package com.segwarez.order.application.rest;

import com.segwarez.order.infrastructure.external.BillingClient;
import com.segwarez.order.infrastructure.external.DeliveryClient;
import com.segwarez.order.infrastructure.external.WarehouseClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderController {
    private final WarehouseClient warehouseClient;
    private final BillingClient billingClient;
    private final DeliveryClient deliveryClient;

    @GetMapping(value = "/order", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> order() {
        if (warehouseClient.reserve().getStatusCode().value() == 409) return ResponseEntity.badRequest().build();
        billingClient.pay();
        return ResponseEntity.ok(deliveryClient.deliver().getBody());
    }
}