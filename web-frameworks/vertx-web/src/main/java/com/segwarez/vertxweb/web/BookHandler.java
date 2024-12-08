package com.segwarez.vertxweb.web;

import com.segwarez.vertxweb.service.Book;
import com.segwarez.vertxweb.service.BookService;
import com.segwarez.vertxweb.web.request.CreateBookRequest;
import com.segwarez.vertxweb.web.request.UpdateBookRequest;
import io.vertx.core.Future;
import io.vertx.ext.web.RoutingContext;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class BookHandler {
    private static final String ID_PARAMETER = "id";
    private static final String TITLE = "title";
    private final BookService bookService;

    public Future<List<Book>> getAllBooks(RoutingContext rc) {
        var pagination = PaginationMapper.fromContext(rc);
        var title = rc.queryParams().get(TITLE);
        if (title == null) {
            return bookService.findAll(pagination).onSuccess(success -> ResponseWrapper.ok(rc, success));
        } else {
            return bookService.findByTitleContaining(title, pagination).onSuccess(success -> ResponseWrapper.ok(rc, success));
        }
    }

    public Future<List<Book>> findByPublished(RoutingContext rc) {
        var pagination = PaginationMapper.fromContext(rc);
        return bookService.findByPublished(true, pagination)
            .onSuccess(success -> ResponseWrapper.ok(rc, success))
            .onFailure(throwable -> ResponseWrapper.error(rc, throwable));
    }

    public Future<Book> getBookById(RoutingContext rc) {
        return bookService.findById(UUID.fromString(rc.pathParam(ID_PARAMETER)))
            .onSuccess(success -> ResponseWrapper.ok(rc, success))
            .onFailure(throwable -> ResponseWrapper.error(rc, throwable));
    }

    public Future<Book> createBook(RoutingContext rc) {
        var request = rc.body().asPojo(CreateBookRequest.class);
        return bookService.create(
                request.getTitle(),
                request.getAuthor(),
                request.getGenre(),
                request.getDescription()
            )
            .onSuccess(success -> ResponseWrapper.created(rc, success.getId()))
            .onFailure(throwable -> ResponseWrapper.error(rc, throwable));
    }

    public Future<Book> updateBook(RoutingContext rc) {
        var request = rc.body().asPojo(UpdateBookRequest.class);
        return bookService.update(
                (UUID.fromString(rc.pathParam(ID_PARAMETER))),
                request.getTitle(),
                request.getAuthor(),
                request.getGenre(),
                request.getDescription(),
                request.isPublished()
            )
            .onSuccess(success -> ResponseWrapper.ok(rc, success))
            .onFailure(throwable -> ResponseWrapper.error(rc, throwable));
    }

    public Future<Void> deleteBook(RoutingContext rc) {
        return bookService.deleteById((UUID.fromString(rc.pathParam(ID_PARAMETER))))
            .onComplete(complete -> ResponseWrapper.noContent(rc));
    }

    public Future<Void> deleteAll(RoutingContext rc) {
        return bookService.deleteAll()
            .onComplete(complete -> ResponseWrapper.noContent(rc));
    }
}
