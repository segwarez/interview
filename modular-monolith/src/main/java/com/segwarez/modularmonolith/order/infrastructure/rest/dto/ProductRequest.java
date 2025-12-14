package com.segwarez.modularmonolith.order.infrastructure.rest.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ProductRequest(
        @NotBlank String productId,
        @Positive BigDecimal unitPrice,
        @Min(1) int quantity
) {}