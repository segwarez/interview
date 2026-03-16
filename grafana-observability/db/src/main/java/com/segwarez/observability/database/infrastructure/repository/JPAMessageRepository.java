package com.segwarez.observability.database.infrastructure.repository;

import com.segwarez.observability.database.infrastructure.repository.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JPAMessageRepository extends JpaRepository<MessageEntity, UUID> {
}