package com.segwarez.vertxweb.web.validation;

import com.segwarez.vertxweb.web.ResponseWrapper;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.validation.BadRequestException;
import io.vertx.ext.web.validation.BodyProcessorException;
import io.vertx.ext.web.validation.ParameterProcessorException;
import io.vertx.ext.web.validation.RequestPredicateException;

public class GlobalErrorHandler {
    private GlobalErrorHandler() {
    }

    public static void addErrorHandler(Router router) {
        router.errorHandler(HttpResponseStatus.BAD_REQUEST.code(), rc -> {
            if (rc.failure() instanceof BadRequestException) {
                if (rc.failure() instanceof ParameterProcessorException) {
                    ResponseWrapper.error(rc, new IllegalArgumentException("Path parameter is invalid"));
                } else if (rc.failure() instanceof BodyProcessorException || rc.failure() instanceof RequestPredicateException) {
                    ResponseWrapper.error(rc, new IllegalArgumentException(rc.failure().getMessage()));
                }
            }
        });
    }
}