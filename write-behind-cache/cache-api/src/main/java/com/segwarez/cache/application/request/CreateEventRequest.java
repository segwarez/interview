package com.segwarez.cache.application.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.time.Instant;

@Value
@AllArgsConstructor
public class CreateEventRequest {
    @NotBlank
    @Size(max = 100)
    String type;
    @NotBlank
    @Size(max = 50)
    String userId;
    @NotNull
    @PastOrPresent
    Instant timestamp;
    @Size(max = 2000)
    String payload;
}