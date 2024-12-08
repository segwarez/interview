package com.segwarez.springweb.domain.repository;

import com.segwarez.springweb.domain.Book;
import com.segwarez.springweb.domain.Pagination;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookRepository {

    List<Book> findAll(Pagination pagination);

    List<Book> findByTitleContaining(String title, Pagination pagination);

    List<Book> findByPublished(boolean published, Pagination pagination);

    Optional<Book> findById(UUID id);

    Book save(Book book);

    void delete(UUID id);

    void deleteAll();
}
