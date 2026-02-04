package com.segwarez.modularmonolith.order.infrastructure.rest.dto;

import com.segwarez.modularmonolith.order.api.PaymentMethod;

public enum PaymentMethodDTO {
    BLIK, CARD, BANK_TRANSFER;

    public PaymentMethod toApi() {
        return PaymentMethod.valueOf(this.name());
    }
}
