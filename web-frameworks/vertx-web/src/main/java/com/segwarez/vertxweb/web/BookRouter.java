package com.segwarez.vertxweb.web;

import com.segwarez.vertxweb.web.validation.BookValidationHandler;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BookRouter {
    private final BookHandler bookHandler;
    private final BookValidationHandler bookValidationHandler;

    public void addBookHandler(Router router) {
        router.route("/books*").handler(BodyHandler.create());
        router.get("/books").handler(bookValidationHandler.getAllBooks()).handler(bookHandler::getAllBooks);
        router.get("/books/published").handler(bookHandler::findByPublished);
        router.get("/books/:id").handler(bookValidationHandler.getBookById()).handler(bookHandler::getBookById);
        router.post("/books").handler(bookValidationHandler.createBook()).handler(bookHandler::createBook);
        router.put("/books/:id").handler(bookValidationHandler.updateBook()).handler(bookHandler::updateBook);
        router.delete("/books/:id").handler(bookValidationHandler.deleteBook()).handler(bookHandler::deleteBook);
        router.delete("/books").handler(bookHandler::deleteAll);
    }
}
