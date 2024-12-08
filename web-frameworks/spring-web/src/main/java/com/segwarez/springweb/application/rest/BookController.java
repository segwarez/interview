package com.segwarez.springweb.application.rest;

import com.segwarez.springweb.application.request.CreateBookRequest;
import com.segwarez.springweb.application.request.UpdateBookRequest;
import com.segwarez.springweb.domain.Book;
import com.segwarez.springweb.domain.Pagination;
import com.segwarez.springweb.domain.SortDirection;
import com.segwarez.springweb.domain.SortOrder;
import com.segwarez.springweb.domain.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Book>> getAllBooks(
            @RequestParam(required = false) String title,
            @PageableDefault(size = 20) Pageable pageable) {
        var pagination = new Pagination(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSort().stream()
                        .map(order -> new SortOrder(
                                order.getProperty(),
                                SortDirection.valueOf(order.getDirection().toString())))
                        .toList());
        if (title == null) return ResponseEntity.ok(bookService.findAll(pagination));
        return ResponseEntity.ok(bookService.findByTitleContaining(title, pagination));
    }

    @GetMapping(value = "/published", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Book>> findPublishedBooks(@PageableDefault(size = 20) Pageable pageable) {
        var pagination = new Pagination(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSort().stream()
                        .map(order -> new SortOrder(
                                order.getProperty(),
                                SortDirection.valueOf(order.getDirection().toString())))
                        .toList());
        return ResponseEntity.ok(bookService.findByPublished(true, pagination));
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Book> getBookById(@PathVariable("id") UUID id) {
        var optionalBook = bookService.findById(id);
        if (optionalBook.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(optionalBook.get());
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Book> createBook(@Valid @RequestBody CreateBookRequest request) {
        var id = bookService.create(
                request.getTitle(),
                request.getAuthor(),
                request.getGenre(),
                request.getDescription());
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(id).toUri()).build();
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Book> updateBook(@PathVariable("id") UUID id, @RequestBody UpdateBookRequest request) {
        var optionalBook = bookService.update(
                id,
                request.getTitle(),
                request.getAuthor(),
                request.getGenre(),
                request.getDescription(),
                request.isPublished());
        if (optionalBook.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(optionalBook.get());
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable("id") UUID id) {
        bookService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping()
    public ResponseEntity<Void> deleteAllBooks() {
        bookService.deleteAll();
        return ResponseEntity.noContent().build();
    }
}
