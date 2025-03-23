package com.segwarez.quarkusweb.domain.service;

import com.segwarez.quarkusweb.domain.Book;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookService {
    List<Book> findAll();

    List<Book> findByTitleContaining(String title);

    List<Book> findByPublished(boolean published);

    Optional<Book> findById(UUID id);

    UUID create(String title, String author, String genre, String description);

    Optional<Book> update(UUID id, String title, String author, String genre, String description, boolean published);

    void deleteById(UUID id);

    void deleteAll();
}
