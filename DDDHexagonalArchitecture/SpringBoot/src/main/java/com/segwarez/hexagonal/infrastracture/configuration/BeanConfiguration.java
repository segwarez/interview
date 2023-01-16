package com.segwarez.hexagonal.infrastracture.configuration;

import com.segwarez.hexagonal.HexagonalApplication;
import com.segwarez.hexagonal.domain.repository.OrderRepository;
import com.segwarez.hexagonal.domain.service.DomainOrderService;
import com.segwarez.hexagonal.domain.service.OrderService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = HexagonalApplication.class)
public class BeanConfiguration {

    @Bean
    OrderService orderService(final OrderRepository orderRepository) {
        return new DomainOrderService(orderRepository);
    }
}
