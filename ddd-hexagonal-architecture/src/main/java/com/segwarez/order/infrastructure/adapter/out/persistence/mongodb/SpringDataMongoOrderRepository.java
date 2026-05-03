package com.segwarez.order.infrastructure.adapter.out.persistence.mongodb;

import com.segwarez.order.infrastructure.adapter.out.persistence.mongodb.document.OrderDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

interface SpringDataMongoOrderRepository extends MongoRepository<OrderDocument, String> {
}