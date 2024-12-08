package com.segwarez.vertxweb.service;

import com.segwarez.vertxweb.repository.BookRepository;
import io.vertx.core.Future;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    public Future<List<Book>> findAll(Pagination pagination) {
        return bookRepository.findAll(pagination);
    }

    public Future<List<Book>> findByTitleContaining(String title, Pagination pagination) {
        return bookRepository.findByTitleContaining(title, pagination);
    }

    public Future<List<Book>> findByPublished(boolean isPublished, Pagination pagination) {
        return bookRepository.findByPublished(isPublished, pagination);
    }

    public Future<Book> findById(UUID id) {
        return bookRepository.findById(id);
    }

    public Future<Book> create(String title, String author, String genre, String description) {
        var book = new Book(title, author, Genre.valueOf(genre), description, false);
        return bookRepository.save(book);
    }

    public Future<Book> update(UUID id, String title, String author, String genre, String description, boolean isPublished) {
        var updatedBook = new Book(id, title, author, Genre.valueOf(genre), description, isPublished);
        return bookRepository.update(updatedBook);
    }

    public Future<Void> deleteById(final UUID id) {
        return bookRepository.delete(id);
    }

    public Future<Void> deleteAll() {
        return bookRepository.deleteAll();
    }
}
