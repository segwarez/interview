package com.segwarez.order.application.command;

import com.segwarez.order.domain.model.order.vo.OrderId;

public record CompleteOrderCommand(
        OrderId orderId
) {}