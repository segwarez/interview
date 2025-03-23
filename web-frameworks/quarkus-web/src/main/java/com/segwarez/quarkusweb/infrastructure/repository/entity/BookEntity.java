package com.segwarez.quarkusweb.infrastructure.repository.entity;

import com.segwarez.quarkusweb.domain.Book;
import com.segwarez.quarkusweb.domain.Genre;
import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "books")
public class BookEntity {
    @Id
    private UUID id;
    private String title;
    private String author;
    @Enumerated(EnumType.STRING)
    private Genre genre;
    private String description;
    private boolean published;

    public BookEntity() {}

    public BookEntity(Book book) {
        this.id = book.id();
        this.title = book.title();
        this.author = book.author();
        this.genre = book.genre();
        this.description = book.description();
        this.published = book.published();
    }

    public Book toBook() {
        return new Book(id, title, author, genre, description, published);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }
}
