package com.segwarez.vertxrx.web;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.json.Json;
import io.vertx.rxjava3.ext.web.RoutingContext;

import java.util.NoSuchElementException;
import java.util.UUID;

public class ResponseWrapper {
    private static final String APPLICATION_JSON = "application/json";
    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String LOCATION = "Location";

    private ResponseWrapper() {
    }

    public static void ok(RoutingContext rc, Object response) {
        rc.response()
            .setStatusCode(HttpResponseStatus.OK.code())
            .putHeader(CONTENT_TYPE_HEADER, APPLICATION_JSON)
            .end(Json.encodePrettily(response));
    }

    public static void created(RoutingContext rc, UUID id) {
        var location = String.format("%s/%s", rc.request().absoluteURI(), id);
        rc.response()
            .setStatusCode(HttpResponseStatus.CREATED.code())
            .putHeader(LOCATION, location)
            .end();
    }

    public static void noContent(RoutingContext rc) {
        rc.response()
            .setStatusCode(HttpResponseStatus.NO_CONTENT.code())
            .end();
    }

    public static void error(RoutingContext routingContext, Throwable throwable) {
        if (throwable instanceof IllegalArgumentException || throwable instanceof IllegalStateException || throwable instanceof NullPointerException) {
            routingContext
                .response()
                .setStatusCode(HttpResponseStatus.BAD_REQUEST.code())
                .end(throwable.getMessage());
        } else if (throwable instanceof NoSuchElementException) {
            routingContext
                .response()
                .setStatusCode(HttpResponseStatus.NOT_FOUND.code())
                .end();
        } else {
            routingContext
                .response()
                .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
                .end("Internal Server Error");
        }
    }
}
