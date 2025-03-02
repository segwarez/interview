package com.segwarez.micronautweb.infrastructure.repository;

import com.segwarez.micronautweb.infrastructure.repository.entity.BookEntity;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.repository.PageableRepository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JPABookRepository extends PageableRepository<BookEntity, UUID> {

    List<BookEntity> findByTitleContains(String title, Pageable pageable);

    List<BookEntity> findByPublished(boolean published, Pageable ppageable);
}
