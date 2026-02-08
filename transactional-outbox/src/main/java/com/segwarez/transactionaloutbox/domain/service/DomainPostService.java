package com.segwarez.transactionaloutbox.domain.service;

import com.segwarez.transactionaloutbox.domain.Post;
import com.segwarez.transactionaloutbox.domain.repository.PostRepository;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class DomainPostService implements PostService {
    private final PostRepository postRepository;

    @Override
    public UUID create(String title, String author, String content) {
        var post = new Post(title, author, content);
        return postRepository.save(post).getId();
    }
}