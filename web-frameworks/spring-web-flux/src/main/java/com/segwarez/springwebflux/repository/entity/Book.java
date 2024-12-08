package com.segwarez.springwebflux.repository.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@NoArgsConstructor
@Table(value = "books")
public class Book {
    @Id
    private UUID id;
    private String title;
    private String author;
    private Genre genre;
    private String description;
    private boolean published;

    public Book(String title, String author, Genre genre, String description, boolean published) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.description = description;
        this.published = published;
    }
}