package com.segwarez.hexagonal.infrastracture.repository;

import com.segwarez.hexagonal.infrastracture.repository.entity.OrderEntity;
import io.micronaut.data.repository.CrudRepository;

import java.util.UUID;

public interface MicronautOrderRepository extends CrudRepository<OrderEntity, UUID> { }
