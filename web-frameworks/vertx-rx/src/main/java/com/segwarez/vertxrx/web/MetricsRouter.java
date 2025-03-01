package com.segwarez.vertxrx.web;

import io.vertx.rxjava3.ext.web.Router;
import io.vertx.rxjava3.micrometer.PrometheusScrapingHandler;

public class MetricsRouter {
    private MetricsRouter() {
    }

    public static void addMetricsHandler(Router router) {
        router.route("/prometheus").handler(PrometheusScrapingHandler.create());
    }
}
