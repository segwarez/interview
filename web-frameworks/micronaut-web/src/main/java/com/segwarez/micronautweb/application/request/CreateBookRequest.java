package com.segwarez.micronautweb.application.request;

import com.segwarez.micronautweb.application.rest.validation.EnumValidator;
import com.segwarez.micronautweb.domain.Genre;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;

@Value
@Introspected
@Serdeable.Deserializable
public class CreateBookRequest {
    @NotNull
    @Size(min=1,max = 255)
    private String title;
    @NotNull
    @Size(min=1,max = 255)
    private String author;
    @EnumValidator(enumClass = Genre.class)
    private String genre;
    @Size(max = 1000)
    private String description;
}
