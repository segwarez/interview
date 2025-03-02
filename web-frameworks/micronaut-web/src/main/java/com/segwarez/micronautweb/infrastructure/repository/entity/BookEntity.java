package com.segwarez.micronautweb.infrastructure.repository.entity;

import com.segwarez.micronautweb.domain.Book;
import com.segwarez.micronautweb.domain.Genre;
import io.micronaut.core.annotation.Introspected;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "books")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
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

    public Book toBook() {
        return new Book(id, title, author, genre, description, published);
    }
}
