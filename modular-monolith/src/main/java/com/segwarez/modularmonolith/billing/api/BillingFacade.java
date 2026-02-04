package com.segwarez.modularmonolith.billing.api;

import java.math.BigDecimal;
import java.util.UUID;

public interface BillingFacade {
    boolean makePayment(UUID orderId, BigDecimal amount, PaymentMethod method);
}