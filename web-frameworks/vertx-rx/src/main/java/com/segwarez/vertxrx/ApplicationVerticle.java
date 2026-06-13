package com.segwarez.vertxrx;

import com.segwarez.vertxrx.configuration.DatabaseConfig;
import com.segwarez.vertxrx.web.BookHandler;
import com.segwarez.vertxrx.web.BookRouter;
import com.segwarez.vertxrx.web.MetricsRouter;
import com.segwarez.vertxrx.web.validation.BookValidationHandler;
import com.segwarez.vertxrx.web.validation.GlobalErrorHandler;
import com.segwarez.vertxrx.service.BookService;
import com.segwarez.vertxrx.repository.BookRepository;
import io.reactivex.rxjava3.core.Completable;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import lombok.extern.slf4j.Slf4j;
import io.vertx.micrometer.MicrometerMetricsOptions;
import io.vertx.micrometer.VertxPrometheusOptions;
import io.vertx.rxjava3.core.AbstractVerticle;
import io.vertx.rxjava3.ext.web.Router;

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
    public Completable rxStart() {
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
        return vertx.createHttpServer()
            .requestHandler(router)
            .rxListen(8080)
            .doOnSuccess(httpServer ->
                log.info(String.format("Application started in %d ms", System.currentTimeMillis() - startTime)))
            .doOnError(throwable ->
                log.info(String.format("Failed to start application %s", throwable.getCause())))
            .ignoreElement();
    }

    @Override
    public Completable rxStop() {
        log.info("Shutting down application");
        return Completable.complete();
    }
}
