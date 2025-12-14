package com.segwarez.modularmonolith.billing.domain;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
public class Payment {
    private final UUID id;
    private final UUID orderId;
    private final BigDecimal amount;
    private final Instant createdAt;
    private PaymentStatus status;

    public Payment(UUID orderId, BigDecimal amount) {
        this.id = UUID.randomUUID();
        this.orderId = orderId;
        this.amount = amount;
        this.createdAt = Instant.now();
        this.status = PaymentStatus.PENDING;
    }

    public void complete() {
        status = PaymentStatus.COMPLETED;
    }

    public void fail() {
        status = PaymentStatus.FAILED;
    }
}