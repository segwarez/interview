package com.segwarez.micronautweb.infrastructure.configuration;

import com.segwarez.micronautweb.domain.repository.BookRepository;
import com.segwarez.micronautweb.domain.service.BookService;
import com.segwarez.micronautweb.domain.service.DomainBookService;
import io.micronaut.context.annotation.Factory;
import jakarta.inject.Singleton;

@Factory
public class BeanConfiguration {
    @Singleton
    BookService bookService(BookRepository bookRepository) {
        return new DomainBookService(bookRepository);
    }
}
