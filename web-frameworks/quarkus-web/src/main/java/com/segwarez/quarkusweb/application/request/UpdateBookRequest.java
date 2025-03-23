package com.segwarez.quarkusweb.application.request;

import com.segwarez.quarkusweb.application.rest.validation.EnumValidator;
import com.segwarez.quarkusweb.domain.Genre;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateBookRequest(
        @NotNull @Size(min = 1, max = 255) String title,
        @NotNull @Size(min = 1, max = 255) String author,
        @EnumValidator(enumClass = Genre.class) String genre,
        @Size(max = 1000) String description,
        boolean published
) {
}