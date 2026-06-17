package com.segwarez.sparkjavaweb.web.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.segwarez.sparkjavaweb.model.Genre;
import com.segwarez.sparkjavaweb.web.validation.EnumValidator;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;

@Value
public class CreateBookRequest {
    @NotNull
    @Size(min=1,max = 255)
    String title;
    @NotNull
    @Size(min=1,max = 255)
    String author;
    @EnumValidator(enumClass = Genre.class)
    String genre;
    @Size(max = 1000)
    String description;

    @JsonCreator
    public CreateBookRequest(
            @JsonProperty("title") String title,
            @JsonProperty("author") String author,
            @JsonProperty("genre") String genre,
            @JsonProperty("description") String description
    ) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.description = description;
    }
}