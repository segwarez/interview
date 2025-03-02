package com.segwarez.micronautweb.application.rest.validation;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Error;
import io.micronaut.http.annotation.Produces;
import jakarta.inject.Singleton;
import jakarta.validation.ConstraintViolationException;

import java.util.HashMap;
import java.util.Map;

@Singleton
public class GlobalExceptionHandler {
    @Error(exception = ConstraintViolationException.class)
    @Produces
    public HttpResponse<Map<String, String>> handleValidationExceptions(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(violation -> {
            String field = violation.getPropertyPath().toString();
            String message = violation.getMessage();
            errors.put(field, message);
        });
        return HttpResponse.badRequest(errors);
    }
}
