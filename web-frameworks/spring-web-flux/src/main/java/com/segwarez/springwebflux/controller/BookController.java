package com.segwarez.springwebflux.controller;

import com.segwarez.springwebflux.controller.request.CreateBookRequest;
import com.segwarez.springwebflux.controller.request.UpdateBookRequest;
import com.segwarez.springwebflux.service.BookService;
import com.segwarez.springwebflux.repository.entity.Book;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import java.net.URI;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Flux<Book> getAllBooks(
            @RequestParam(required = false) String title,
            @PageableDefault(size = 20) Pageable pageable) {
        if (title == null) return bookService.findAll(pageable);
        else return bookService.findByTitleContaining(title, pageable);
    }

    @GetMapping(value = "/published", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Flux<Book> findPublishedBooks(@PageableDefault(size = 20) Pageable pageable) {
        return bookService.findByPublished(true, pageable);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Book>> getBookById(@PathVariable("id") UUID id) {
        return bookService.findById(id).map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Book>> createBook(@Valid @RequestBody CreateBookRequest createBookRequest, ServerHttpRequest request) {
        return bookService.create(
                createBookRequest.getTitle(),
                createBookRequest.getAuthor(),
                createBookRequest.getGenre(),
                createBookRequest.getDescription()
        ).map(book -> {
            URI location = UriComponentsBuilder.newInstance().uri(request.getURI()).path("/{id}").buildAndExpand(book.getId()).toUri();
            return ResponseEntity.created(location).build();
        });
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Book>> updateBook(@PathVariable("id") UUID id, @RequestBody UpdateBookRequest updateBookRequest) {
        return bookService.update(
                id,
                updateBookRequest.getTitle(),
                updateBookRequest.getAuthor(),
                updateBookRequest.getGenre(),
                updateBookRequest.getDescription(),
                updateBookRequest.isPublished()
        ).map(ResponseEntity::ok).switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteBook(@PathVariable("id") UUID id) {
        return bookService.deleteById(id);
    }

    @DeleteMapping()
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteAllBooks() {
        return bookService.deleteAll();
    }
}
