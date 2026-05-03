package com.segwarez.order.application.port.in;

import com.segwarez.order.application.command.RemoveOrderItemCommand;
import com.segwarez.order.application.dto.OrderDto;

public interface RemoveOrderItemUseCase {
    OrderDto removeItem(RemoveOrderItemCommand command);
}