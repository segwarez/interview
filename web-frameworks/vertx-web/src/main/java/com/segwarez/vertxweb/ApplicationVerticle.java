package com.segwarez.vertxweb;

import com.segwarez.vertxweb.configuration.DatabaseConfig;
import com.segwarez.vertxweb.web.BookHandler;
import com.segwarez.vertxweb.web.BookRouter;
import com.segwarez.vertxweb.web.MetricsRouter;
import com.segwarez.vertxweb.web.validation.BookValidationHandler;
import com.segwarez.vertxweb.web.validation.GlobalErrorHandler;
import com.segwarez.vertxweb.service.BookService;
import com.segwarez.vertxweb.repository.BookRepository;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.config.MeterFilter;
import io.micrometer.core.instrument.distribution.DistributionStatisticConfig;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.micrometer.MicrometerMetricsOptions;
import io.vertx.micrometer.VertxPrometheusOptions;

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
    public void start(Promise<Void> promise) {
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
        vertx.createHttpServer()
            .requestHandler(router)
            .listen(8080, http -> {
                if (http.succeeded()) {
                    promise.complete();
                    LOGGER.info(String.format("Application started in %d ms", System.currentTimeMillis() - startTime));
                } else {
                    promise.fail(http.cause());
                    LOGGER.info("Failed to start application");
                }
            });
    }

    @Override
    public void stop() {
        LOGGER.info("Shutting down application");
    }
}
