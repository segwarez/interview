package com.segwarez.order.infrastructure.external;

import org.springframework.http.ResponseEntity;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange(accept = "application/json")
public interface BillingClient {
    @PostExchange("/pay")
    ResponseEntity<String> pay();
}