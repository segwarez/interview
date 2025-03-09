package com.segwarez.springweb.infrastructure.configuration;

import com.segwarez.springweb.SpringWebApplication;
import com.segwarez.springweb.domain.repository.BookRepository;
import com.segwarez.springweb.domain.service.BookService;
import com.segwarez.springweb.domain.service.DomainBookService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = SpringWebApplication.class)
public class BeanConfiguration {
    @Bean
    BookService bookService(BookRepository bookRepository) {
        return new DomainBookService(bookRepository);
    }
}
