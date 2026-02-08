package com.segwarez.transactionaloutbox.infrastructure.repository;

import com.segwarez.transactionaloutbox.infrastructure.repository.entity.OutboxEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JPAOutboxRepository extends CrudRepository<OutboxEntity, UUID> {
    List<OutboxEntity> findTop50ByProcessed(boolean processed);
}
