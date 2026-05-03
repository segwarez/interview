package com.segwarez.order.infrastructure.adapter.in.messaging;


import com.segwarez.order.application.command.CreateOrderCommand;
import com.segwarez.order.application.port.in.CreateOrderUseCase;
import com.segwarez.order.domain.model.product.vo.ProductId;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static com.segwarez.order.infrastructure.configuration.KafkaConfig.ORDER_COMMANDS_TOPIC;

@Component
class CreateOrderConsumer {
    private final CreateOrderUseCase createOrderUseCase;

    CreateOrderConsumer(CreateOrderUseCase createOrderUseCase) {
        this.createOrderUseCase = createOrderUseCase;
    }

    @KafkaListener(topics = ORDER_COMMANDS_TOPIC, groupId = "order-service")
    void consume(CreateOrderMessage command) {
        createOrderUseCase.createOrder(
                new CreateOrderCommand(
                        ProductId.of(command.productId()),
                        command.quantity()
                )
        );
    }
}