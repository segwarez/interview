package com.segwarez.quarkusweb.domain.repository;

import com.segwarez.quarkusweb.domain.Book;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookRepository {

    List<Book> findAll();

    List<Book> findByTitleContaining(String title);

    List<Book> findByPublished(boolean published);

    Optional<Book> findById(UUID id);

    Book save(Book book);

    Optional<Book> update(Book book);

    void delete(UUID id);

    void deleteAll();
}
