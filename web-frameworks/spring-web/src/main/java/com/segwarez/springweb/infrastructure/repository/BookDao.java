package com.segwarez.springweb.infrastructure.repository;

import com.segwarez.springweb.domain.Book;
import com.segwarez.springweb.domain.Pagination;
import com.segwarez.springweb.domain.repository.BookRepository;
import com.segwarez.springweb.infrastructure.repository.entity.BookEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BookDao implements BookRepository {
    private final JPABookRepository jpaBookRepository;

    @Override
    public List<Book> findAll(Pagination pagination) {
        var sort = pagination.getSortOrders().stream()
                .map(sortOrder -> new Sort.Order(
                        Sort.Direction.valueOf(sortOrder.getDirection().toString()),
                        sortOrder.getField())).toList();
        var pageRequest = PageRequest.of(
                pagination.getPageNumber(),
                pagination.getPageSize(),
                Sort.by(sort));
        return jpaBookRepository.findBy(pageRequest).stream().map(BookEntity::toBook).toList();
    }

    @Override
    public List<Book> findByTitleContaining(String title, Pagination pagination) {
        var sort = pagination.getSortOrders().stream()
                .map(sortOrder -> new Sort.Order(
                        Sort.Direction.valueOf(sortOrder.getDirection().toString()),
                        sortOrder.getField())).toList();
        var pageRequest = PageRequest.of(
                pagination.getPageNumber(),
                pagination.getPageSize(),
                Sort.by(sort));
        return jpaBookRepository.findByTitleContaining(title, pageRequest).stream()
                .map(BookEntity::toBook)
                .toList();
    }

    @Override
    public List<Book> findByPublished(boolean published, Pagination pagination) {
        var sort = pagination.getSortOrders().stream()
                .map(sortOrder -> new Sort.Order(
                        Sort.Direction.valueOf(sortOrder.getDirection().toString()),
                        sortOrder.getField())).toList();
        var pageRequest = PageRequest.of(
                pagination.getPageNumber(),
                pagination.getPageSize(),
                Sort.by(sort));
        return jpaBookRepository.findByPublished(published, pageRequest).stream()
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
