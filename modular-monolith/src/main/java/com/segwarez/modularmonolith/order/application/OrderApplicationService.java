package com.segwarez.modularmonolith.order.application;

import com.segwarez.modularmonolith.billing.api.BillingFacade;
import com.segwarez.modularmonolith.billing.api.PaymentMethod;
import com.segwarez.modularmonolith.delivery.api.DeliveryFacade;
import com.segwarez.modularmonolith.delivery.api.DeliveryInfo;
import com.segwarez.modularmonolith.delivery.api.ShippingAddress;
import com.segwarez.modularmonolith.order.api.OrderConfirmation;
import com.segwarez.modularmonolith.order.api.OrderFacade;
import com.segwarez.modularmonolith.order.api.PlaceOrderCommand;
import com.segwarez.modularmonolith.order.domain.Order;
import com.segwarez.modularmonolith.order.domain.OrderItem;
import com.segwarez.modularmonolith.order.domain.OrderShippingAddress;
import com.segwarez.modularmonolith.order.infrastructure.repository.OrderRepository;
import com.segwarez.modularmonolith.warehouse.api.WarehouseFacade;
import com.segwarez.modularmonolith.warehouse.api.WarehouseProductItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.segwarez.modularmonolith.order.application.mapper.DeliveryShipmentAddressMapper.toDelivery;

@Service
@RequiredArgsConstructor
public class OrderApplicationService implements OrderFacade {
    private final WarehouseFacade warehouseFacade;
    private final BillingFacade billingFacade;
    private final DeliveryFacade deliveryFacade;
    private final OrderRepository orderRepository;

    @Override
    public OrderConfirmation placeOrder(PlaceOrderCommand command) {
        List<OrderItem> items = command.getProducts().stream()
                .map(OrderItem::new)
                .toList();

        Order order = new Order(items, command.getShippingAddress().toDomain(), command.getPaymentMethod().toDomain());

        List<WarehouseProductItem> warehouseItems = command.getProducts().stream()
                .map(p -> new WarehouseProductItem(p.getProductId(), p.getQuantity()))
                .toList();

        boolean reserved = warehouseFacade.reserveProducts(warehouseItems);
        if (!reserved) {
            throw new IllegalStateException("Insufficient stock for products");
        }

        boolean paid = billingFacade.makePayment(order.getId(), order.getTotalAmount(), PaymentMethod.valueOf(order.getPaymentMethod().name()));
        if (!paid) {
            throw new IllegalStateException("Payment failed for order " + order.getId());
        }
        order.markPaid();

        DeliveryInfo deliveryInfo = deliveryFacade.scheduleDelivery(order.getId(), toDelivery(order.getShippingAddress()));
        order.markShipped();

        orderRepository.save(order);

        return new OrderConfirmation(
                order.getId(),
                order.getItems(),
                order.getTotalAmount(),
                deliveryInfo.getTrackingNumber(),
                deliveryInfo.getEstimatedDeliveryTime()
        );
    }
}