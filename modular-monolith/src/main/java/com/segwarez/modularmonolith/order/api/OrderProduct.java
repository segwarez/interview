package com.segwarez.modularmonolith.order.api;

import lombok.Value;

import java.math.BigDecimal;
import java.util.UUID;

@Value
public class OrderProduct {
    UUID productId;
    BigDecimal unitPrice;
    int quantity;
}