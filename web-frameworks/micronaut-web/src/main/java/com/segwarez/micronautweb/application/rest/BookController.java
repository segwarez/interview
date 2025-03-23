package com.segwarez.micronautweb.application.rest;

import com.segwarez.micronautweb.application.request.CreateBookRequest;
import com.segwarez.micronautweb.application.request.UpdateBookRequest;
import com.segwarez.micronautweb.domain.Book;
import com.segwarez.micronautweb.domain.Pagination;
import com.segwarez.micronautweb.domain.SortDirection;
import com.segwarez.micronautweb.domain.SortOrder;
import com.segwarez.micronautweb.domain.service.BookService;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller("/books")
@ExecuteOn(TaskExecutors.VIRTUAL)
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @Get(produces = MediaType.APPLICATION_JSON)
    public HttpResponse<List<Book>> getAllBooks(
            @QueryValue Optional<String> title,
            Pageable pageable) {
        var pagination = new Pagination(
                pageable.getNumber(),
                pageable.getSize(),
                pageable.getSort().getOrderBy().stream()
                        .map(order -> new SortOrder(
                                order.getProperty(),
                                SortDirection.valueOf(order.getDirection().toString())))
                        .toList());
        if (title.isEmpty()) return HttpResponse.ok(bookService.findAll(pagination));
        return HttpResponse.ok(bookService.findByTitleContaining(title.get(), pagination));
    }

    @Get(value = "/published", produces = MediaType.APPLICATION_JSON)
    public HttpResponse<List<Book>> findPublishedBooks(Pageable pageable) {
        var pagination = new Pagination(
                pageable.getNumber(),
                pageable.getSize(),
                pageable.getSort().getOrderBy().stream()
                        .map(order -> new SortOrder(
                                order.getProperty(),
                                SortDirection.valueOf(order.getDirection().toString())))
                        .toList());
        return HttpResponse.ok(bookService.findByPublished(true, pagination));
    }

    @Get(value = "/{id}", produces = MediaType.APPLICATION_JSON)
    public HttpResponse<Book> getBookById(@PathVariable("id") UUID id) {
        var optionalBook = bookService.findById(id);
        if (optionalBook.isEmpty()) return HttpResponse.notFound();
        return HttpResponse.ok(optionalBook.get());
    }

    @Post(produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
    public HttpResponse<Book> createBook(@Body @Valid CreateBookRequest createBookRequest, HttpRequest request) {
        var id = bookService.create(
                createBookRequest.getTitle(),
                createBookRequest.getAuthor(),
                createBookRequest.getGenre(),
                createBookRequest.getDescription());
        return HttpResponse.created(URI.create(request.getPath() + "/" + id));
    }

    @Put(value = "/{id}", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
    public HttpResponse<Book> updateBook(@PathVariable("id") UUID id, @Body UpdateBookRequest request) {
        var optionalBook = bookService.update(
                id,
                request.getTitle(),
                request.getAuthor(),
                request.getGenre(),
                request.getDescription(),
                request.isPublished());
        if (optionalBook.isEmpty()) return HttpResponse.notFound();
        return HttpResponse.ok(optionalBook.get());
    }

    @Delete(value = "/{id}")
    public HttpResponse<Void> deleteBook(@PathVariable("id") UUID id) {
        bookService.deleteById(id);
        return HttpResponse.noContent();
    }

    @Delete
    public HttpResponse<Void> deleteAllBooks() {
        bookService.deleteAll();
        return HttpResponse.noContent();
    }
}
