package com.segwarez.modularmonolith.order.application;

import com.segwarez.modularmonolith.billing.api.BillingFacade;
import com.segwarez.modularmonolith.delivery.api.DeliveryFacade;
import com.segwarez.modularmonolith.delivery.api.DeliveryInfo;
import com.segwarez.modularmonolith.order.api.OrderConfirmation;
import com.segwarez.modularmonolith.order.api.OrderFacade;
import com.segwarez.modularmonolith.order.api.PlaceOrderCommand;
import com.segwarez.modularmonolith.order.domain.Order;
import com.segwarez.modularmonolith.order.domain.OrderItem;
import com.segwarez.modularmonolith.order.infrastructure.repository.OrderRepository;
import com.segwarez.modularmonolith.warehouse.api.WarehouseFacade;
import com.segwarez.modularmonolith.warehouse.api.WarehouseProductItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderApplicationService implements OrderFacade {
    private final WarehouseFacade warehouseFacade;
    private final BillingFacade billingFacade;
    private final DeliveryFacade deliveryFacade;
    private final OrderRepository orderRepository;

    @Override
    public OrderConfirmation placeOrder(PlaceOrderCommand command) {
        List<OrderItem> items = command.products().stream()
                .map(OrderItem::new)
                .toList();

        Order order = new Order(items, command.destinationAddress());

        List<WarehouseProductItem> warehouseItems = command.products().stream()
                .map(p -> new WarehouseProductItem(p.productId(), p.quantity()))
                .toList();

        boolean reserved = warehouseFacade.reserve(warehouseItems);
        if (!reserved) {
            throw new IllegalStateException("Insufficient stock for products");
        }

        boolean paid = billingFacade.makePayment(order.getId(), order.getTotalAmount());
        if (!paid) {
            throw new IllegalStateException("Payment failed for order " + order.getId());
        }
        order.markPaid();

        DeliveryInfo deliveryInfo =
                deliveryFacade.scheduleDelivery(order.getId(), order.getDestinationAddress());
        order.markShipped();

        orderRepository.save(order);

        return new OrderConfirmation(
                order.getId(),
                order.getItems(),
                order.getTotalAmount(),
                deliveryInfo.trackingNumber(),
                deliveryInfo.estimatedDeliveryTime()
        );
    }
}