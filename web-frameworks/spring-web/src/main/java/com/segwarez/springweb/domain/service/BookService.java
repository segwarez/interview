package com.segwarez.springweb.domain.service;

import com.segwarez.springweb.domain.Book;
import com.segwarez.springweb.domain.Pagination;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookService {
    List<Book> findAll(Pagination pagination);

    List<Book> findByTitleContaining(String title, Pagination pagination);

    List<Book> findByPublished(boolean published, Pagination pagination);

    Optional<Book> findById(UUID id);

    UUID create(String title, String author, String genre, String description);

    Optional<Book> update(UUID id, String title, String author, String genre, String description, boolean published);

    void deleteById(UUID id);

    void deleteAll();
}
