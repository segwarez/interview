package com.segwarez.order.infrastructure.external;

import org.springframework.http.ResponseEntity;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange(accept = "application/json")
public interface DeliveryClient {
    @GetExchange("/deliver")
    ResponseEntity<String> deliver();
}