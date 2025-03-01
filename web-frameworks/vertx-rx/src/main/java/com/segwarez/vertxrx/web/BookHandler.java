package com.segwarez.vertxrx.web;

import com.segwarez.vertxrx.service.BookService;
import com.segwarez.vertxrx.web.request.CreateBookRequest;
import com.segwarez.vertxrx.web.request.UpdateBookRequest;
import io.vertx.rxjava3.ext.web.RoutingContext;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class BookHandler {
    private static final String ID_PARAMETER = "id";
    private static final String TITLE = "title";
    private final BookService bookService;

    public void getAllBooks(RoutingContext rc) {
        var pagination = PaginationMapper.fromContext(rc);
        var title = rc.queryParams().get(TITLE);
        if (title == null) {
            bookService.findAll(pagination).toList()
                .subscribe(
                    books -> ResponseWrapper.ok(rc, books)
                );
        } else {
            bookService.findByTitleContaining(title, pagination).toList()
                .subscribe(
                    books -> ResponseWrapper.ok(rc, books)
                );
        }
    }

    public void findByPublished(RoutingContext rc) {
        var pagination = PaginationMapper.fromContext(rc);
        bookService.findByPublished(true, pagination).toList()
            .subscribe(
                books -> ResponseWrapper.ok(rc, books),
                throwable -> ResponseWrapper.error(rc, throwable)
            );
    }

    public void getBookById(RoutingContext rc) {
        bookService.findById(UUID.fromString(rc.pathParam(ID_PARAMETER)))
            .subscribe(
                book -> ResponseWrapper.ok(rc, book),
                throwable -> ResponseWrapper.error(rc, throwable)
            );
    }

    public void createBook(RoutingContext rc) {
        var request = rc.body().asPojo(CreateBookRequest.class);
        bookService.create(
            request.getTitle(),
            request.getAuthor(),
            request.getGenre(),
            request.getDescription()
        )
        .subscribe(
            book -> ResponseWrapper.created(rc, book.getId()),
            throwable -> ResponseWrapper.error(rc, throwable)
        );
    }

    public void updateBook(RoutingContext rc) {
        var request = rc.body().asPojo(UpdateBookRequest.class);
        bookService.update(
            (UUID.fromString(rc.pathParam(ID_PARAMETER))),
            request.getTitle(),
            request.getAuthor(),
            request.getGenre(),
            request.getDescription(),
            request.isPublished()
        ).subscribe(
            book -> ResponseWrapper.ok(rc, book),
            throwable -> ResponseWrapper.error(rc, throwable)
        );
    }

    public void deleteBook(RoutingContext rc) {
        bookService.deleteById((UUID.fromString(rc.pathParam(ID_PARAMETER))))
            .subscribe(
                complete -> ResponseWrapper.noContent(rc)
            );
    }

    public void deleteAll(RoutingContext rc) {
        bookService.deleteAll()
            .subscribe(
                complete -> ResponseWrapper.noContent(rc)
            );
    }
}
