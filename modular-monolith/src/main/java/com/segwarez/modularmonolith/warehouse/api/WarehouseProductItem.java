package com.segwarez.modularmonolith.warehouse.api;

import lombok.Value;

import java.util.UUID;

@Value
public class WarehouseProductItem {
    UUID productId;
    int quantity;
}