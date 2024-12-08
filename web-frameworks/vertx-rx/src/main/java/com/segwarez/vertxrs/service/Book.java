package com.segwarez.vertxrs.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    private UUID id;
    private String title;
    private String author;
    private Genre genre;
    private String description;

    private boolean published;

    public Book(String title, String author, Genre genre, String description, boolean published) {
        this.id = UUID.randomUUID();
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.description = description;
        this.published = published;
    }
}
