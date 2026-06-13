package com.segwarez.transactionaloutbox.infrastructure.repository;

import com.segwarez.transactionaloutbox.domain.Post;
import com.segwarez.transactionaloutbox.domain.repository.PostRepository;
import com.segwarez.transactionaloutbox.infrastructure.repository.entity.EventType;
import com.segwarez.transactionaloutbox.infrastructure.repository.entity.OutboxEntity;
import com.segwarez.transactionaloutbox.infrastructure.repository.entity.PostEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@AllArgsConstructor
public class PostDao implements PostRepository {
    private final JPAPostRepository jpaPostRepository;
    private final JPAOutboxRepository jpaOutboxRepository;

    @Transactional
    public Post save(Post post) {
        var savedPost = jpaPostRepository.save(new PostEntity(post)).toDomain();
        jpaOutboxRepository.save(new OutboxEntity(EventType.POST_CREATED, savedPost.getId()));
        return savedPost;
    }
}