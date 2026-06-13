package com.segwarez.vertxweb;

import com.segwarez.vertxweb.configuration.DatabaseConfig;
import com.segwarez.vertxweb.web.BookHandler;
import com.segwarez.vertxweb.web.BookRouter;
import com.segwarez.vertxweb.web.MetricsRouter;
import com.segwarez.vertxweb.web.validation.BookValidationHandler;
import com.segwarez.vertxweb.web.validation.GlobalErrorHandler;
import com.segwarez.vertxweb.service.BookService;
import com.segwarez.vertxweb.repository.BookRepository;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.ext.web.Router;
import io.vertx.micrometer.MicrometerMetricsOptions;
import io.vertx.micrometer.VertxPrometheusOptions;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ApplicationVerticle extends AbstractVerticle {
    private static long startTime = System.currentTimeMillis();
    static void main(String[] args) {
        MicrometerMetricsOptions metricsOptions = new MicrometerMetricsOptions()
            .setPrometheusOptions(new VertxPrometheusOptions().setEnabled(true))
            .setJvmMetricsEnabled(true)
            .setEnabled(true);

        Vertx vertx = Vertx.vertx(new VertxOptions().setMetricsOptions(metricsOptions));
        vertx.deployVerticle(new ApplicationVerticle());
    }

    @Override
    public void start(Promise<Void> promise) {
        var dbClient = DatabaseConfig.buildDbClient(vertx);
        var bookRepository = new BookRepository(dbClient);
        var bookService = new BookService(bookRepository);
        var bookHandler = new BookHandler(bookService);
        var bookValidationHandler = new BookValidationHandler();
        var bookRouter = new BookRouter(bookHandler, bookValidationHandler);
        var router = Router.router(vertx);
        GlobalErrorHandler.addErrorHandler(router);
        MetricsRouter.addMetricsHandler(router);
        bookRouter.addBookHandler(router);
        vertx.createHttpServer()
            .requestHandler(router)
            .listen(8080)
            .onSuccess(httpServer -> {
                log.info("Application started in {} ms", System.currentTimeMillis() - startTime);
                promise.complete();
            })
            .onFailure(throwable -> {
                log.error("Failed to start application", throwable);
                promise.fail(throwable);
            });
    }

    @Override
    public void stop() {
        log.info("Shutting down application");
    }
}
