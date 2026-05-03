package com.segwarez.order.application.port.in;

import com.segwarez.order.application.command.CreateOrderCommand;
import com.segwarez.order.application.dto.OrderDto;

public interface CreateOrderUseCase {
    OrderDto createOrder(CreateOrderCommand command);
}