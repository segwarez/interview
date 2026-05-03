package com.segwarez.order.application.port.in;

import com.segwarez.order.application.command.ApplyDiscountCommand;
import com.segwarez.order.application.dto.OrderDto;

public interface ApplyDiscountUseCase {
    OrderDto applyDiscount(ApplyDiscountCommand command);
}