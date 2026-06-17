package com.segwarez.sparkjavaweb;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.segwarez.sparkjavaweb.config.ApplicationConfig;
import com.segwarez.sparkjavaweb.config.DatabaseConfig;
import com.segwarez.sparkjavaweb.config.JsonTransformer;
import com.segwarez.sparkjavaweb.model.BookService;
import com.segwarez.sparkjavaweb.repository.BookRepository;
import com.segwarez.sparkjavaweb.web.BookController;
import com.segwarez.sparkjavaweb.web.validation.ValidationException;
import io.micrometer.core.instrument.binder.jetty.JettyConnectionMetrics;
import io.micrometer.core.instrument.binder.jvm.ClassLoaderMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;
import io.micrometer.core.instrument.binder.system.UptimeMetrics;
import io.micrometer.prometheusmetrics.PrometheusConfig;
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry;

import static spark.Spark.*;

public class Application {
    private static final String APPLICATION_JSON = "application/json";

    static void main(String[] args) {
        ApplicationConfig config = ApplicationConfig.load();
        port(config.server().getPort());

        var dataSource = DatabaseConfig.dataSource(config);
        var dsl = DatabaseConfig.dslContext(dataSource);

        var prometheusRegistry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
        new JettyConnectionMetrics(prometheusRegistry);
        new JvmMemoryMetrics().bindTo(prometheusRegistry);
        new JvmGcMetrics().bindTo(prometheusRegistry);
        new JvmThreadMetrics().bindTo(prometheusRegistry);
        new ClassLoaderMetrics().bindTo(prometheusRegistry);
        new ProcessorMetrics().bindTo(prometheusRegistry);
        new UptimeMetrics().bindTo(prometheusRegistry);

        var objectMapper = new ObjectMapper();

        var jsonTransformer = new JsonTransformer(objectMapper);
        var bookRepository = new BookRepository(dsl);
        var bookService = new BookService(bookRepository);
        var bookController = new BookController(bookService, objectMapper);

        before((request, response) -> response.type(APPLICATION_JSON));

        exception(ValidationException.class, (exception, request, response) -> {
            response.status(400);
            response.body("""
                {
                  "message": "%s"
                }
                """.formatted(exception.getMessage()));
        });

        get("/health", (request, response) -> "OK");
        get("/prometheus", (request, response) -> {
            response.type("text/plain");
            return prometheusRegistry.scrape();
        });

        get("/books", bookController::getAllBooks, jsonTransformer);
        get("/books/published", bookController::findByPublished, jsonTransformer);
        get("/books/:id", bookController::getBookById, jsonTransformer);
        post("/books", bookController::createBook, jsonTransformer);
        put("/books/:id", bookController::updateBook, jsonTransformer);
        delete("/books/:id", bookController::deleteBook, jsonTransformer);
        delete("/books", bookController::deleteAll, jsonTransformer);

        Runtime.getRuntime().addShutdownHook(new Thread(dataSource::close));
    }
}