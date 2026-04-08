package com.segwarez.cache.api.infrastructure.repository;

import com.segwarez.cache.api.infrastructure.repository.entity.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JPAEventRepository extends JpaRepository<EventEntity, UUID> {
}