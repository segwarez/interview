package com.segwarez.quarkusweb.domain;

import java.util.UUID;

public record Book(UUID id, String title, String author, Genre genre, String description, boolean published) {

    public Book(String title, String author, Genre genre, String description, boolean published) {
        this(UUID.randomUUID(), title, author, genre, description, published);
    }
}