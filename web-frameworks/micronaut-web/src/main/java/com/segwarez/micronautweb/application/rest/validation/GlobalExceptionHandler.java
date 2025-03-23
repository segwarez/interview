package com.segwarez.micronautweb.application.rest.validation;

import io.micronaut.context.annotation.Primary;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;
import jakarta.validation.ConstraintViolationException;

import java.util.HashMap;
import java.util.Map;

@Primary
@Produces
@Singleton
@Requires(
        classes = {ConstraintViolationException.class, ExceptionHandler.class}
)
public class GlobalExceptionHandler implements ExceptionHandler<ConstraintViolationException, HttpResponse<?>> {

    @Override
    public HttpResponse<?> handle(HttpRequest request, ConstraintViolationException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getConstraintViolations().forEach(violation -> {
            String field = violation.getPropertyPath().toString();
            String message = violation.getMessage();
            errors.put(field, message);
        });
        return HttpResponse.badRequest(errors);
    }
}
