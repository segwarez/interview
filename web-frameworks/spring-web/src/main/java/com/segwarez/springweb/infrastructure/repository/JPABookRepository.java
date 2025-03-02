package com.segwarez.springweb.infrastructure.repository;

import com.segwarez.springweb.infrastructure.repository.entity.BookEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JPABookRepository extends CrudRepository<BookEntity, UUID>,PagingAndSortingRepository<BookEntity, UUID> {
    List<BookEntity> findBy(PageRequest pageRequest);

    List<BookEntity> findByTitleContaining(String title, PageRequest pageRequest);

    List<BookEntity> findByPublished(boolean published, PageRequest pageRequest);
}
