package com.segwarez.modularmonolith.order.infrastructure.rest;

import com.segwarez.modularmonolith.order.infrastructure.rest.response.OrderErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice(assignableTypes = OrderController.class)
public class OrderExceptionHandler {
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<OrderErrorResponse> handleIllegalState(IllegalStateException ex) {
        return ResponseEntity.badRequest().body(
                new OrderErrorResponse(Instant.now(), 400, "ORDER_ERROR", ex.getMessage())
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<OrderErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        var msg = ex.getBindingResult().getFieldErrors().stream()
                .findFirst().map(e -> e.getField() + " " + e.getDefaultMessage())
                .orElse("Validation error");

        return ResponseEntity.badRequest().body(
                new OrderErrorResponse(Instant.now(), 400, "VALIDATION_ERROR", msg)
        );
    }
}