package com.segwarez.order.application.port.in;

import com.segwarez.order.application.command.CompleteOrderCommand;
import com.segwarez.order.application.dto.OrderDto;

public interface CompleteOrderUseCase {
    OrderDto completeOrder(CompleteOrderCommand command);
}