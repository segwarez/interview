package com.segwarez.modularmonolith.warehouse.domain;

import lombok.Getter;

import java.util.UUID;

@Getter
public class StockItem {
    private final UUID productId;
    private int availableQuantity;

    public StockItem(UUID productId, int availableQuantity) {
        this.productId = productId;
        this.availableQuantity = availableQuantity;
    }

    public boolean check(int quantity) {
        return availableQuantity >= quantity;
    }

    public void decrease(int quantity) {
        availableQuantity -= quantity;
    }
}

