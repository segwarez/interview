package com.segwarez.modularmonolith.order.api;

import com.segwarez.modularmonolith.order.domain.OrderPaymentMethod;

public enum PaymentMethod {
    BLIK, CARD, BANK_TRANSFER;

    public OrderPaymentMethod toDomain() {
        return OrderPaymentMethod.valueOf(this.name());
    }
}
