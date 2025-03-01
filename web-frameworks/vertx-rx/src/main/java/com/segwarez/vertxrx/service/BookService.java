package com.segwarez.vertxrx.service;

import com.segwarez.vertxrx.repository.BookRepository;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    public Flowable<Book> findAll(Pagination pagination) {
        return bookRepository.findAll(pagination);
    }

    public Flowable<Book> findByTitleContaining(String title, Pagination pagination) {
        return bookRepository.findByTitleContaining(title, pagination);
    }

    public Flowable<Book> findByPublished(boolean isPublished, Pagination pagination) {
        return bookRepository.findByPublished(isPublished, pagination);
    }

    public Single<Book> findById(UUID id) {
        return bookRepository.findById(id);
    }

    public Single<Book> create(String title, String author, String genre, String description) {
        var book = new Book(title, author, Genre.valueOf(genre), description, false);
        return bookRepository.save(book);
    }

    public Maybe<Book> update(UUID id, String title, String author, String genre, String description, boolean isPublished) {
        var updatedBook = new Book(id, title, author, Genre.valueOf(genre), description, isPublished);
        return bookRepository.update(updatedBook);
    }

    public Single<Integer> deleteById(final UUID id) {
        return bookRepository.delete(id);
    }

    public Single<Integer> deleteAll() {
        return bookRepository.deleteAll();
    }
}
