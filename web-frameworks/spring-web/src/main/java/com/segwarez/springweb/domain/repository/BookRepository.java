package com.segwarez.springweb.domain.repository;

import com.segwarez.springweb.domain.Book;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookRepository {

    List<Book> findAll();

    List<Book> findByTitleContaining(String title);

    List<Book> findByPublished(boolean published);

    Optional<Book> findById(UUID id);

    Book save(Book book);

    void delete(UUID id);

    void deleteAll();
}
