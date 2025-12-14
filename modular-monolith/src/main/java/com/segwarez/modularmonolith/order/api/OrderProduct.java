package com.segwarez.modularmonolith.order.api;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderProduct(
        UUID productId,
        BigDecimal unitPrice,
        int quantity
) {}
