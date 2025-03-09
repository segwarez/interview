package com.segwarez.springweb.application.request;

import com.segwarez.springweb.application.rest.validation.EnumValidator;
import com.segwarez.springweb.domain.Genre;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class UpdateBookRequest {
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
    private boolean published;
}
