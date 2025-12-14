package com.segwarez.modularmonolith.order.api;

import java.util.List;

public record PlaceOrderCommand(
        List<OrderProduct> products,
        String destinationAddress
) {}
