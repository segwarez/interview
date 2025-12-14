package com.segwarez.modularmonolith.order.infrastructure.rest.dto;

import java.time.Instant;

public record OrderErrorResponse(
        Instant timestamp,
        int status,
        String error,
        String message
) {}
