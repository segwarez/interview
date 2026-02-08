package com.segwarez.transactionaloutbox.infrastructure.repository.entity;

import com.segwarez.transactionaloutbox.domain.Post;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@Table(name = "post")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class PostEntity {
    @Id
    @EqualsAndHashCode.Include
    private UUID id;
    private String title;
    private String author;
    private String content;

    public PostEntity(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.author = post.getAuthor();
        this.content = post.getContent();
    }

    public Post toDomain() {
        return new Post(id, title, author, content);
    }
}