package com.segwarez.order.application.command;

import com.segwarez.order.domain.model.discount.vo.DiscountCode;
import com.segwarez.order.domain.model.order.vo.OrderId;

public record ApplyDiscountCommand(
        OrderId orderId,
        DiscountCode discountCode
) {}