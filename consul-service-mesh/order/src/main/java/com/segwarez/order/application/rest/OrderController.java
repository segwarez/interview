package com.segwarez.order.application.rest;

import com.segwarez.order.infrastructure.external.BillingClient;
import com.segwarez.order.infrastructure.external.DeliveryClient;
import com.segwarez.order.infrastructure.external.WarehouseClient;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderController {
    private static final Logger log = LoggerFactory.getLogger(OrderController.class);
    private final WarehouseClient warehouseClient;
    private final BillingClient billingClient;
    private final DeliveryClient deliveryClient;

    @GetMapping(value = "/order", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> order() {
        log.info("Order received");
        if (warehouseClient.reserve().getStatusCode().value() == 409) {
            log.info("Stock not available");
            return ResponseEntity.badRequest().build();
        }
        log.info("Stock reserved");
        billingClient.pay();
        log.info("Payment done");
        return ResponseEntity.ok(deliveryClient.deliver().getBody());
    }
}