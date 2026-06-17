package com.segwarez.sparkjavaweb.web.validation;

import jakarta.validation.Validation;
import jakarta.validation.Validator;

import java.util.stream.Collectors;

public final class RequestValidator {
    private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();

    private RequestValidator() {
    }

    public static <T> void validate(T request) {
        var violations = VALIDATOR.validate(request);
        if (!violations.isEmpty()) {
            String message = violations.stream()
                    .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                    .collect(Collectors.joining(", "));

            throw new ValidationException(message);
        }
    }
}