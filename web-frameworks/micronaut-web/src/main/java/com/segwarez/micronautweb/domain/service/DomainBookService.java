package com.segwarez.micronautweb.domain.service;


import com.segwarez.micronautweb.domain.Book;
import com.segwarez.micronautweb.domain.Genre;
import com.segwarez.micronautweb.domain.Pagination;
import com.segwarez.micronautweb.domain.repository.BookRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class DomainBookService implements BookService {
    private final BookRepository bookRepository;

    public List<Book> findAll(Pagination pagination) {
        return bookRepository.findAll(pagination);
    }

    public List<Book> findByTitleContaining(String title, Pagination pagination) {
        return bookRepository.findByTitleContaining(title, pagination);
    }

    public List<Book> findByPublished(boolean published, Pagination pagination) {
        return bookRepository.findByPublished(published, pagination);
    }

    public Optional<Book> findById(UUID id) {
        return bookRepository.findById(id);
    }

    @Override
    public UUID create(String title, String author, String genre, String description) {
        var book = new Book(title, author, Genre.valueOf(genre), description, false);
        bookRepository.save(book);
        return book.getId();
    }

    public Optional<Book> update(UUID id, String title, String author, String genre, String description, boolean published) {
        var optionalBook = bookRepository.findById(id);
        if (optionalBook.isEmpty()) return Optional.empty();
        var book = optionalBook.get();
        book.setTitle(title);
        book.setAuthor(author);
        book.setGenre(Genre.valueOf(genre));
        book.setDescription(description);
        book.setPublished(published);
        return Optional.of(bookRepository.save(book));
    }

    @Override
    public void deleteById(UUID id) {
        bookRepository.delete(id);
    }

    public void deleteAll() {
        bookRepository.deleteAll();
    }
}
