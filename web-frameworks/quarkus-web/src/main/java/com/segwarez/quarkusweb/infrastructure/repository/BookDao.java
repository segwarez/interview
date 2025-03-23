package com.segwarez.quarkusweb.infrastructure.repository;

import com.segwarez.quarkusweb.domain.Book;
import com.segwarez.quarkusweb.domain.repository.BookRepository;
import com.segwarez.quarkusweb.infrastructure.configuration.Pagination;
import com.segwarez.quarkusweb.infrastructure.repository.entity.BookEntity;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Singleton
public class BookDao implements BookRepository {
    @Inject
    private PanacheBookRepository panacheBookRepository;
    @Inject
    private Pagination pagination;

    @Override
    public List<Book> findAll() {
        return panacheBookRepository.findAll(pagination).stream().map(BookEntity::toBook).toList();
    }

    @Override
    public List<Book> findByTitleContaining(String title) {
        return panacheBookRepository.findByTitleContaining(title, pagination).stream()
                .map(BookEntity::toBook)
                .toList();
    }

    @Override
    public List<Book> findByPublished(boolean published) {
        return panacheBookRepository.findByPublished(published, pagination).stream()
                .map(BookEntity::toBook)
                .toList();
    }

    @Override
    public Optional<Book> findById(UUID id) {
        var entity = panacheBookRepository.findByIdOptional(id);
        return entity.map(BookEntity::toBook);
    }

    @Override
    public Book save(Book book) {
        panacheBookRepository.persist(new BookEntity(book));
        return book;
    }

    @Override
    public Optional<Book> update(Book book) {
        var optionalEntity = panacheBookRepository.findByIdOptional(book.id());
        if (optionalEntity.isEmpty()) return Optional.empty();
        var bookEntity = optionalEntity.get();
        bookEntity.setAuthor(book.author());
        bookEntity.setGenre(book.genre());
        bookEntity.setDescription(book.description());
        bookEntity.setPublished(book.published());
        panacheBookRepository.persist(bookEntity);
        return Optional.of(book);
    }

    @Override
    public void delete(UUID id) {
        panacheBookRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        panacheBookRepository.deleteAll();
    }
}
