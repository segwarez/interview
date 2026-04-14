package com.segwarez.cache.persistence;

import com.segwarez.cache.persistence.entity.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JPAEventRepository extends JpaRepository<EventEntity, UUID> {
}