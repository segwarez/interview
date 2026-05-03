package com.segwarez.order.application.port.out;

import com.segwarez.order.domain.model.order.Order;
import com.segwarez.order.domain.model.order.vo.OrderId;

import java.util.Optional;

public interface OrderRepository {
    Optional<Order> findById(OrderId orderId);
    Order save(Order order);
}