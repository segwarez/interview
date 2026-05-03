package com.segwarez.order.application.query;

import com.segwarez.order.domain.model.order.vo.OrderId;

public record GetOrderQuery(
        OrderId orderId
) {}