package com.segwarez.springweb.infrastructure.repository.entity;

import com.segwarez.springweb.domain.Book;
import com.segwarez.springweb.domain.Genre;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@Table(name = "books")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class BookEntity {
    @Id
    @EqualsAndHashCode.Include
    private UUID id;
    private String title;
    private String author;
    @Enumerated(EnumType.STRING)
    private Genre genre;
    private String description;
    private boolean published;

    public BookEntity(Book book) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.author = book.getAuthor();
        this.genre = book.getGenre();
        this.description = book.getDescription();
        this.published = book.isPublished();
    }

    public Book toBook() {
        return new Book(id, title, author, genre, description, published);
    }
}
