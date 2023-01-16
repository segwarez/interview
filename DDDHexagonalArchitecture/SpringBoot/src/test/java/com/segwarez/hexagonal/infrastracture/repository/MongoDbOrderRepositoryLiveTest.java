package com.segwarez.hexagonal.infrastracture.repository;

import com.segwarez.hexagonal.domain.Order;
import com.segwarez.hexagonal.domain.Product;
import com.segwarez.hexagonal.domain.repository.OrderRepository;
import com.segwarez.hexagonal.infrastracture.repository.mongo.SpringDataMongoOrderRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringJUnitConfig
@SpringBootTest
class MongoDbOrderRepositoryLiveTest {

    @Autowired
    private SpringDataMongoOrderRepository mongoOrderRepository;

    @Autowired
    private OrderRepository orderRepository;

    @AfterEach
    void cleanUp() {
        mongoOrderRepository.deleteAll();
    }

    @Test
    void shouldFindById_thenReturnOrder() {

        // given
        final UUID id = UUID.randomUUID();
        final Order order = createOrder(id);

        // when
        orderRepository.save(order);

        final Optional<Order> result = orderRepository.findById(id);

        assertEquals(order, result.get());
    }

    private Order createOrder(UUID id) {
        return new Order(id, new Product(UUID.randomUUID(), BigDecimal.TEN, "product"));
    }
}
