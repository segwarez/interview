package com.segwarez.quarkusweb.infrastructure.configuration;

import com.segwarez.quarkusweb.domain.repository.BookRepository;
import com.segwarez.quarkusweb.domain.service.BookService;
import com.segwarez.quarkusweb.domain.service.DomainBookService;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Produces;

@Dependent
public class BeanConfiguration {
    @Produces
    BookService bookService(BookRepository bookRepository) {
        return new DomainBookService(bookRepository);
    }
}
