package com.segwarez.modularmonolith.delivery.api;

import java.util.UUID;

public interface DeliveryFacade {
    DeliveryInfo scheduleDelivery(UUID orderId, ShippingAddress shippingAddress);
}
