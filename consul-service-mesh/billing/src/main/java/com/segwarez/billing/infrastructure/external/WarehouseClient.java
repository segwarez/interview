package com.segwarez.billing.infrastructure.external;

import org.springframework.http.ResponseEntity;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PutExchange;

@HttpExchange(accept = "application/json")
public interface WarehouseClient {
    @PutExchange("/update")
    ResponseEntity<String> update();
}