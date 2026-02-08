package com.segwarez.transactionaloutbox.infrastructure.configuration;

import com.segwarez.transactionaloutbox.TransactionalOutboxApplication;
import com.segwarez.transactionaloutbox.domain.repository.PostRepository;
import com.segwarez.transactionaloutbox.domain.service.DomainPostService;
import com.segwarez.transactionaloutbox.domain.service.PostService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = TransactionalOutboxApplication.class)
public class BeanConfiguration {
    @Bean
    PostService postService(PostRepository postRepository) {
        return new DomainPostService(postRepository);
    }
}