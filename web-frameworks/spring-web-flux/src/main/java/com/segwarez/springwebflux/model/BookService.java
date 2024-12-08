package com.segwarez.springwebflux.model;

import com.segwarez.springwebflux.repository.BookRepository;
import com.segwarez.springwebflux.repository.entity.Book;
import com.segwarez.springwebflux.repository.entity.Genre;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    public Flux<Book> findAll(Pageable pageable) {
        return bookRepository.findBy(pageable);
    }

    public Flux<Book> findByTitleContaining(String title, Pageable pageable) {
        return bookRepository.findByTitleContaining(title, pageable);
    }

    public Flux<Book> findByPublished(boolean published, Pageable pageable) {
        return bookRepository.findByPublished(published, pageable);
    }

    public Mono<Book> findById(UUID id) {
        return bookRepository.findById(id);
    }

    public Mono<Book> create(String title, String author, String genre, String description) {
        var book = new Book(title, author, Genre.valueOf(genre), description, false);
        return bookRepository.save(book);
    }

    public Mono<Book> update(UUID id, String title, String author, String genre, String description, boolean published) {
        return bookRepository.findById(id).map(Optional::of).defaultIfEmpty(Optional.empty()).flatMap(optionalBook -> {
            if (optionalBook.isEmpty()) return Mono.empty();
            var book = optionalBook.get();
            book.setTitle(title);
            book.setAuthor(author);
            book.setGenre(Genre.valueOf(genre));
            book.setDescription(description);
            book.setPublished(published);
            return bookRepository.save(book);
        });
    }

    public Mono<Void> deleteById(UUID id) {
        return bookRepository.deleteById(id);
    }

    public Mono<Void> deleteAll() {
        return bookRepository.deleteAll();
    }
}
