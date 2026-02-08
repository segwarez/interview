package com.segwarez.transactionaloutbox.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class Post {
    private UUID id;
    private String title;
    private String author;
    private String content;

    public Post(String title, String author, String content) {
        this.id = UUID.randomUUID();
        this.title = title;
        this.author = author;
        this.content = content;
    }
}