package com.segwarez.sparkjavaweb.model;

import com.segwarez.sparkjavaweb.repository.BookRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    public List<Book> findAll(Pagination pagination) {
        return bookRepository.findAll(pagination);
    }

    public List<Book> findByTitleContaining(String title, Pagination pagination) {
        return bookRepository.findByTitleContaining(title, pagination);
    }

    public List<Book> findByPublished(boolean isPublished, Pagination pagination) {
        return bookRepository.findByPublished(isPublished, pagination);
    }

    public Optional<Book> findById(UUID id) {
        return bookRepository.findById(id);
    }

    public Book create(String title, String author, String genre, String description) {
        var book = new Book(title, author, Genre.valueOf(genre), description, false);
        return bookRepository.save(book);
    }

    public Optional<Book> update(UUID id, String title, String author, String genre, String description, boolean isPublished) {
        var updatedBook = new Book(id, title, author, Genre.valueOf(genre), description, isPublished);
        return bookRepository.update(updatedBook);
    }

    public void deleteById(final UUID id) {
        bookRepository.delete(id);
    }

    public void deleteAll() {
        bookRepository.deleteAll();
    }
}