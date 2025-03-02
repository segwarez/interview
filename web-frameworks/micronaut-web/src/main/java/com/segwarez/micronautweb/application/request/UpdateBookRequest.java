package com.segwarez.micronautweb.application.request;

import com.segwarez.micronautweb.application.rest.validation.EnumValidator;
import com.segwarez.micronautweb.domain.Genre;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
@Serdeable.Deserializable
public class UpdateBookRequest {
    @NotNull
    private String title;
    @NotNull
    private String author;
    @EnumValidator(enumClass = Genre.class)
    private String genre;
    @Size(max = 5)
    private String description;
    private boolean published;
}
