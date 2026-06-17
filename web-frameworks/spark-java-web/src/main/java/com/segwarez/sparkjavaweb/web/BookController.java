package com.segwarez.sparkjavaweb.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.segwarez.sparkjavaweb.model.BookService;
import com.segwarez.sparkjavaweb.web.request.CreateBookRequest;
import com.segwarez.sparkjavaweb.web.request.UpdateBookRequest;
import lombok.RequiredArgsConstructor;
import spark.Request;
import spark.Response;

import java.util.UUID;

import static com.segwarez.sparkjavaweb.web.validation.RequestValidator.validate;

@RequiredArgsConstructor
public class BookController {
    private static final String LOCATION = "Location";
    private static final String ID_PARAMETER = "id";
    private static final String TITLE = "title";
    private final BookService bookService;
    private final ObjectMapper objectMapper;

    public Object getAllBooks(Request request, Response response) {
        var pagination = PaginationMapper.fromRequest(request);
        var title = request.queryParams(TITLE);

        if (title == null) return bookService.findAll(pagination);
        return bookService.findByTitleContaining(title, pagination);
    }

    public Object findByPublished(Request request, Response response) {
        var pagination = PaginationMapper.fromRequest(request);
        return bookService.findByPublished(true, pagination);
    }

    public Object getBookById(Request request, Response response) {
        var id = UUID.fromString(request.params(ID_PARAMETER));
        var optionalBook = bookService.findById(id);
        if (optionalBook.isEmpty()) {
            response.status(404);
            return null;
        }
        return optionalBook.get();
    }

    public Object createBook(Request request, Response response) throws JsonProcessingException {
        var createBookRequest = objectMapper.readValue(request.body(), CreateBookRequest.class);
        validate(createBookRequest);

        var book = bookService.create(
                createBookRequest.getTitle(),
                createBookRequest.getAuthor(),
                createBookRequest.getGenre(),
                createBookRequest.getDescription()
        );

        response.status(201);
        response.header(LOCATION, request.scheme() + "://" + request.host() + request.uri() + "/" + book.getId());
        return null;
    }

    public Object updateBook(Request request, Response response) throws JsonProcessingException {
        var id = UUID.fromString(request.params(ID_PARAMETER));
        var updateBookRequest = objectMapper.readValue(request.body(), UpdateBookRequest.class);
        validate(updateBookRequest);

        var optionalBook = bookService.update(
                id,
                updateBookRequest.getTitle(),
                updateBookRequest.getAuthor(),
                updateBookRequest.getGenre(),
                updateBookRequest.getDescription(),
                updateBookRequest.isPublished()
        );

        if (optionalBook.isEmpty()) {
            response.status(404);
            return null;
        }
        return optionalBook.get();
    }

    public Object deleteBook(Request request, Response response) {
        var id = UUID.fromString(request.params(ID_PARAMETER));
        bookService.deleteById(id);
        response.status(204);
        return null;
    }

    public Object deleteAll(Request request, Response response) {
        bookService.deleteAll();
        response.status(204);
        return null;
    }
}