package com.segwarez.vertxrs.web.validation;

import com.segwarez.vertxrs.web.ResponseWrapper;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.ext.web.validation.BadRequestException;
import io.vertx.ext.web.validation.BodyProcessorException;
import io.vertx.ext.web.validation.ParameterProcessorException;
import io.vertx.ext.web.validation.RequestPredicateException;
import io.vertx.rxjava3.ext.web.Router;

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
