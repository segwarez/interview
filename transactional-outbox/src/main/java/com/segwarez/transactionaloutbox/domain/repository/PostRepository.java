package com.segwarez.transactionaloutbox.domain.repository;

import com.segwarez.transactionaloutbox.domain.Post;

public interface PostRepository {
    Post save(Post post);
}