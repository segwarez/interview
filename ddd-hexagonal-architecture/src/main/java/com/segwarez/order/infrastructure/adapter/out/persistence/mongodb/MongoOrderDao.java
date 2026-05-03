package com.segwarez.order.infrastructure.adapter.out.persistence.mongodb;

import com.segwarez.order.domain.model.order.Order;
import com.segwarez.order.domain.model.order.vo.OrderId;
import com.segwarez.order.application.port.out.OrderRepository;
import com.segwarez.order.infrastructure.adapter.out.persistence.mongodb.mapper.OrderDocumentMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
class MongoOrderDao implements OrderRepository {
    private final SpringDataMongoOrderRepository repository;
    private final OrderDocumentMapper mapper;

    MongoOrderDao(SpringDataMongoOrderRepository repository, OrderDocumentMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Order save(Order order) {
        var document = mapper.toDocument(order);
        var saved = repository.save(document);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Order> findById(OrderId orderId) {
        return repository.findById(orderId.asString())
                .map(mapper::toDomain);
    }
}