package com.segwarez.springwebflux.controller.request;

import com.segwarez.springwebflux.controller.validation.EnumValidator;
import com.segwarez.springwebflux.repository.entity.Genre;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
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
