package com.segwarez.quarkusweb.domain.service;


import com.segwarez.quarkusweb.domain.Book;
import com.segwarez.quarkusweb.domain.Genre;
import com.segwarez.quarkusweb.domain.repository.BookRepository;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class DomainBookService implements BookService {
    private final BookRepository bookRepository;

    public DomainBookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

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
        return bookRepository.save(book).id();
    }

    public Optional<Book> update(UUID id, String title, String author, String genre, String description, boolean published) {
        var updatedBook = new Book(id, title, author, Genre.valueOf(genre), description, published);
        return bookRepository.update(updatedBook);
    }

    @Override
    public void deleteById(UUID id) {
        bookRepository.delete(id);
    }

    public void deleteAll() {
        bookRepository.deleteAll();
    }
}
