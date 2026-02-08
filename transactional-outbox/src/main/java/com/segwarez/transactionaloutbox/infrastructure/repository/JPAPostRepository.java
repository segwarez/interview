package com.segwarez.transactionaloutbox.infrastructure.repository;

import com.segwarez.transactionaloutbox.infrastructure.repository.entity.PostEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JPAPostRepository extends CrudRepository<PostEntity, UUID> {
}