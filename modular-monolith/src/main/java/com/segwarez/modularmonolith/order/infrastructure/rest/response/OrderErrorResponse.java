package com.segwarez.modularmonolith.order.infrastructure.rest.response;

import lombok.Value;

import java.time.Instant;

@Value
public class OrderErrorResponse {
    Instant timestamp;
    int status;
    String error;
    String message;
}