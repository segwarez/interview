package com.segwarez.transactionaloutbox.domain.service;

import java.util.UUID;

public interface PostService {
    UUID create(String title, String author, String content);
}