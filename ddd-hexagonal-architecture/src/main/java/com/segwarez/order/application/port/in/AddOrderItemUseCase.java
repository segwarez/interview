package com.segwarez.order.application.port.in;

import com.segwarez.order.application.command.AddOrderItemCommand;
import com.segwarez.order.application.dto.OrderDto;

public interface AddOrderItemUseCase {
    OrderDto addItem(AddOrderItemCommand command);
}