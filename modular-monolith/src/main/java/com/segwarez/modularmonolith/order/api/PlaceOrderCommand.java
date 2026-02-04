package com.segwarez.modularmonolith.order.api;

import lombok.Value;

import java.util.List;

@Value
public class PlaceOrderCommand {
    List<OrderProduct> products;
    ShippingAddress shippingAddress;
    PaymentMethod paymentMethod;
}
