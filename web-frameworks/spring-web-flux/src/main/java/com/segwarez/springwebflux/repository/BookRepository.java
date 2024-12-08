package com.segwarez.springwebflux.repository;

import com.segwarez.springwebflux.repository.entity.Book;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface BookRepository extends R2dbcRepository<Book, UUID> {
    Flux<Book> findBy(Pageable pageable);

    Flux<Book> findByTitleContaining(String title, Pageable pageable);

    Flux<Book> findByPublished(boolean published, Pageable pageable);
}