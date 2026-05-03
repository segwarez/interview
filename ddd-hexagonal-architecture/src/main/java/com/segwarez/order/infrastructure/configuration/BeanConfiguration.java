package com.segwarez.order.infrastructure.configuration;

import com.segwarez.order.application.port.out.OrderRepository;
import com.segwarez.order.application.port.out.ProductRepository;
import com.segwarez.order.application.service.OrderService;
import com.segwarez.order.domain.model.discount.DiscountEvaluator;
import com.segwarez.order.domain.model.discount.rule.OrderDiscountRule;
import com.segwarez.order.domain.model.discount.rule.ProductSpecificDiscountRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.util.List;

@Configuration
public class BeanConfiguration {

    @Bean
    Clock clock() {
        return Clock.systemDefaultZone();
    }

    @Bean
    DiscountEvaluator discountEvaluator(Clock clock) {
        return new DiscountEvaluator(List.of(
                new OrderDiscountRule(),
                new ProductSpecificDiscountRule()
        ), clock);
    }

    @Bean
    OrderService orderService(
            OrderRepository orderRepository,
            ProductRepository productRepository,
            DiscountEvaluator discountEvaluator) {
        return new OrderService(
                orderRepository,
                productRepository,
                discountEvaluator
        );
    }
}