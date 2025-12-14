package com.segwarez.modularmonolith.order.api;

public interface OrderFacade {
    OrderConfirmation placeOrder(PlaceOrderCommand command);
}