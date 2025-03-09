package com.segwarez.springweb.infrastructure.repository;

import com.segwarez.springweb.domain.Book;
import com.segwarez.springweb.domain.repository.BookRepository;
import com.segwarez.springweb.infrastructure.configuration.Pagination;
import com.segwarez.springweb.infrastructure.repository.entity.BookEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BookDao implements BookRepository {
    private final JPABookRepository jpaBookRepository;
    private final Pagination pagination;

    @Override
    public List<Book> findAll() {
        var pageable = pagination.getPageable();
        return jpaBookRepository.findAll(pageable).stream().map(BookEntity::toBook).toList();
    }

    @Override
    public List<Book> findByTitleContaining(String title) {
        var pageable = pagination.getPageable();
        return jpaBookRepository.findByTitleContaining(title, pageable).stream()
                .map(BookEntity::toBook)
                .toList();
    }

    @Override
    public List<Book> findByPublished(boolean published) {
        var pageable = pagination.getPageable();
        return jpaBookRepository.findByPublished(published, pageable).stream()
                .map(BookEntity::toBook)
                .toList();
    }

    @Override
    public Optional<Book> findById(UUID id) {
        var entity = jpaBookRepository.findById(id);
        return entity.map(BookEntity::toBook);
    }

    @Override
    public Book save(Book book) {
        return jpaBookRepository.save(new BookEntity(book)).toBook();
    }

    @Override
    public void delete(UUID id) {
        jpaBookRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        jpaBookRepository.deleteAll();
    }
}
