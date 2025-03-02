package com.segwarez.micronautweb.infrastructure.repository;

import com.segwarez.micronautweb.domain.Book;
import com.segwarez.micronautweb.domain.Pagination;
import com.segwarez.micronautweb.domain.repository.BookRepository;
import com.segwarez.micronautweb.infrastructure.repository.entity.BookEntity;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.model.Sort;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Singleton
@RequiredArgsConstructor
public class BookDao implements BookRepository {
    private final JPABookRepository jpaBookRepository;

    @Override
    public List<Book> findAll(Pagination pagination) {
        var orders = pagination.getSortOrders().stream()
                .map(sortOrder -> new Sort.Order(
                        sortOrder.getField(),
                        Sort.Order.Direction.valueOf(sortOrder.getDirection().toString()),true)).toList();
        var pageable = Pageable.from(
                pagination.getPageNumber(),
                pagination.getPageSize(),
                Sort.of(orders));
        return jpaBookRepository.findAll(pageable).getContent().stream()
                .map(BookEntity::toBook)
                .toList();
    }

    @Override
    public List<Book> findByTitleContaining(String title, Pagination pagination) {
        var orders = pagination.getSortOrders().stream()
                .map(sortOrder -> new Sort.Order(
                        sortOrder.getField(),
                        Sort.Order.Direction.valueOf(sortOrder.getDirection().toString()),true)).toList();
        var pageable = Pageable.from(
                pagination.getPageNumber(),
                pagination.getPageSize(),
                Sort.of(orders));
        return jpaBookRepository.findByTitleContains(title, pageable).stream()
                .map(BookEntity::toBook)
                .toList();
    }

    @Override
    public List<Book> findByPublished(boolean published, Pagination pagination) {
        var orders = pagination.getSortOrders().stream()
                .map(sortOrder -> new Sort.Order(
                        sortOrder.getField(),
                        Sort.Order.Direction.valueOf(sortOrder.getDirection().toString()),true)).toList();
        var pageable = Pageable.from(
                pagination.getPageNumber(),
                pagination.getPageSize(),
                Sort.of(orders));
        return jpaBookRepository.findByPublished(published, pageable).stream()
                .map(BookEntity::toBook)
                .toList();
    }

    @Override
    public Optional<Book> findById(UUID id) {
        var entity = jpaBookRepository.findById(id);
        if (entity.isEmpty()) return Optional.empty();
        return Optional.of((entity.get().toBook()));

    }

    @Override
    public Book save(Book book) {
        return jpaBookRepository.save(new BookEntity(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getGenre(),
                book.getDescription(),
                book.isPublished())).toBook();
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
