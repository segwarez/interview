package com.segwarez.modularmonolith.order.infrastructure.rest.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record PlaceOrderRequest(
        @NotEmpty @Valid List<ProductRequest> products,
        @NotBlank String destinationAddress
) {}
