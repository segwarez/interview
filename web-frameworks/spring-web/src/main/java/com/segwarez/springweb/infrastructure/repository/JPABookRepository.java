package com.segwarez.springweb.infrastructure.repository;

import com.segwarez.springweb.infrastructure.repository.entity.BookEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JPABookRepository extends JpaRepository<BookEntity, UUID> {

    List<BookEntity> findByTitleContaining(String title, Pageable pageable);

    List<BookEntity> findByPublished(boolean published, Pageable pageable);
}
