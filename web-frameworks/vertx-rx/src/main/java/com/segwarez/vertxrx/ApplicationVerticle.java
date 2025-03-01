package com.segwarez.vertxrx;

import com.segwarez.vertxrx.configuration.DatabaseConfig;
import com.segwarez.vertxrx.web.BookHandler;
import com.segwarez.vertxrx.web.BookRouter;
import com.segwarez.vertxrx.web.MetricsRouter;
import com.segwarez.vertxrx.web.validation.BookValidationHandler;
import com.segwarez.vertxrx.web.validation.GlobalErrorHandler;
import com.segwarez.vertxrx.service.BookService;
import com.segwarez.vertxrx.repository.BookRepository;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.config.MeterFilter;
import io.micrometer.core.instrument.distribution.DistributionStatisticConfig;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.reactivex.rxjava3.core.Completable;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.micrometer.MicrometerMetricsOptions;
import io.vertx.micrometer.VertxPrometheusOptions;
import io.vertx.rxjava3.core.AbstractVerticle;
import io.vertx.rxjava3.ext.web.Router;

public class ApplicationVerticle extends AbstractVerticle {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationVerticle.class);
    private static long startTime = System.currentTimeMillis();

    public static void main(String[] args) {
        PrometheusMeterRegistry prometheusRegistry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
        prometheusRegistry.config().meterFilter(
            new MeterFilter() {
                @Override
                public DistributionStatisticConfig configure(Meter.Id id, DistributionStatisticConfig config) {
                    return DistributionStatisticConfig.builder()
                        .percentilesHistogram(true)
                        .build()
                        .merge(config);
                }
            });
        MicrometerMetricsOptions metricsOptions = new MicrometerMetricsOptions()
            .setPrometheusOptions(new VertxPrometheusOptions().setEnabled(true))
            .setMicrometerRegistry(prometheusRegistry)
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
        var bookValidationHandler = new BookValidationHandler(vertx);
        var bookRouter = new BookRouter(bookHandler, bookValidationHandler);
        var router = Router.router(vertx);
        GlobalErrorHandler.addErrorHandler(router);
        MetricsRouter.addMetricsHandler(router);
        bookRouter.addBookHandler(router);
        return vertx.createHttpServer()
            .requestHandler(router)
            .rxListen(8080)
            .doOnSuccess(httpServer ->
                LOGGER.info(String.format("Application started in %d ms", System.currentTimeMillis() - startTime)))
            .doOnError(throwable ->
                LOGGER.info(String.format("Failed to start application %s", throwable.getCause())))
            .ignoreElement();
    }

    @Override
    public Completable rxStop() {
        LOGGER.info("Shutting down application");
        return Completable.complete();
    }
}
