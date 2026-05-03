package com.segwarez.order.application.mapper;

import com.segwarez.order.application.dto.OrderItemDto;
import com.segwarez.order.application.dto.OrderDto;
import com.segwarez.order.domain.model.order.Order;
import com.segwarez.order.domain.model.order.OrderItem;

public class OrderDtoMapper {
    public static OrderDto toDto(Order order) {
        return new OrderDto(
                order.id().value(),
                order.status().name(),
                order.items().stream()
                        .map(OrderDtoMapper::toItemDto)
                        .toList()
        );
    }

    private static OrderItemDto toItemDto(OrderItem item) {
        return new OrderItemDto(
                item.productId().value(),
                item.productName().value(),
                item.quantity(),
                item.unitPrice().amount(),
                item.unitPrice().currency().getCurrencyCode(),
                item.discount().isNone() ? null : item.discount().code().value(),
                item.discount().isNone() ? 0 : item.discount().percentage().value()
        );
    }
}