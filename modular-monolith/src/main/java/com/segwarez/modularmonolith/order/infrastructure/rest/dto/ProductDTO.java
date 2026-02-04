package com.segwarez.modularmonolith.order.infrastructure.rest.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Value;

import java.math.BigDecimal;

@Value
public class ProductDTO {
    @NotBlank
    String productId;
    @Positive
    BigDecimal unitPrice;
    @Min(1)
    int quantity;
}