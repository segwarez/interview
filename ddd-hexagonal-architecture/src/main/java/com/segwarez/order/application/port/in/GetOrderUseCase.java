package com.segwarez.order.application.port.in;

import com.segwarez.order.application.query.GetOrderQuery;
import com.segwarez.order.application.dto.OrderDto;

public interface GetOrderUseCase {
    OrderDto getOrder(GetOrderQuery query);
}