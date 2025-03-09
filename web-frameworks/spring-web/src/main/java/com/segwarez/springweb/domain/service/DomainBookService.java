package com.segwarez.springweb.domain.service;


import com.segwarez.springweb.domain.Book;
import com.segwarez.springweb.domain.Genre;
import com.segwarez.springweb.domain.repository.BookRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class DomainBookService implements BookService {
    private final BookRepository bookRepository;

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public List<Book> findByTitleContaining(String title) {
        return bookRepository.findByTitleContaining(title);
    }

    public List<Book> findByPublished(boolean published) {
        return bookRepository.findByPublished(published);
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
