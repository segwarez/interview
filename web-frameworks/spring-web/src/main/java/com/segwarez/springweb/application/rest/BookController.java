package com.segwarez.springweb.application.rest;

import com.segwarez.springweb.application.request.CreateBookRequest;
import com.segwarez.springweb.application.request.UpdateBookRequest;
import com.segwarez.springweb.domain.Book;
import com.segwarez.springweb.domain.service.BookService;
import com.segwarez.springweb.infrastructure.configuration.Pagination;
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
    private final Pagination pagination;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Book>> getAllBooks(
            @RequestParam(required = false) String title,
            @PageableDefault(size = 20) Pageable pageable) {
        pagination.setPageable(pageable);
        if (title == null) return ResponseEntity.ok(bookService.findAll());
        return ResponseEntity.ok(bookService.findByTitleContaining(title));
    }

    @GetMapping(value = "/published", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Book>> findPublishedBooks(@PageableDefault(size = 20) Pageable pageable) {
        pagination.setPageable(pageable);
        return ResponseEntity.ok(bookService.findByPublished(true));
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Book> getBookById(@PathVariable("id") UUID id) {
        var optionalBook = bookService.findById(id);
        return optionalBook.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
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
        return optionalBook.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
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
