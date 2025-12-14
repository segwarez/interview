package com.segwarez.modularmonolith.warehouse.api;

import java.util.UUID;

public record WarehouseProductItem(
        UUID productId,
        int quantity
) {}
