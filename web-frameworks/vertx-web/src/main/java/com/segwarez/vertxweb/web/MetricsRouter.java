package com.segwarez.vertxweb.web;

import io.vertx.ext.web.Router;
import io.vertx.micrometer.PrometheusScrapingHandler;

public class MetricsRouter {
    private MetricsRouter() {
    }

    public static void addMetricsHandler(Router router) {
        router.route("/prometheus").handler(PrometheusScrapingHandler.create());
    }
}
