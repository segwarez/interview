package com.segwarez.sparkjavaweb.repository;

import com.segwarez.sparkjavaweb.model.Book;
import com.segwarez.sparkjavaweb.model.Genre;
import com.segwarez.sparkjavaweb.model.Pagination;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.SortField;
import org.jooq.Table;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.name;
import static org.jooq.impl.DSL.table;

@RequiredArgsConstructor
public class BookRepository {
    private static final Table<?> BOOKS = table(name("books"));
    private static final Field<UUID> ID = field(name("id"), UUID.class);
    private static final Field<String> TITLE = field(name("title"), String.class);
    private static final Field<String> AUTHOR = field(name("author"), String.class);
    private static final Field<String> GENRE = field(name("genre"), String.class);
    private static final Field<String> DESCRIPTION = field(name("description"), String.class);
    private static final Field<Boolean> PUBLISHED = field(name("published"), Boolean.class);
    private final DSLContext dsl;

    public List<Book> findAll(Pagination pagination) {
        return dsl.select(ID, TITLE, AUTHOR, GENRE, DESCRIPTION, PUBLISHED)
                .from(BOOKS)
                .orderBy(buildSortFields(pagination))
                .limit(pagination.getPageSize())
                .offset(pagination.getPageNumber() * pagination.getPageSize())
                .fetch(this::mapToBook);
    }

    public List<Book> findByTitleContaining(String title, Pagination pagination) {
        return dsl.select(ID, TITLE, AUTHOR, GENRE, DESCRIPTION, PUBLISHED)
                .from(BOOKS)
                .where(TITLE.containsIgnoreCase(title))
                .orderBy(buildSortFields(pagination))
                .limit(pagination.getPageSize())
                .offset(pagination.getPageNumber() * pagination.getPageSize())
                .fetch(this::mapToBook);
    }

    public List<Book> findByPublished(boolean isPublished, Pagination pagination) {
        return dsl.select(ID, TITLE, AUTHOR, GENRE, DESCRIPTION, PUBLISHED)
                .from(BOOKS)
                .where(PUBLISHED.eq(isPublished))
                .orderBy(buildSortFields(pagination))
                .limit(pagination.getPageSize())
                .offset(pagination.getPageNumber() * pagination.getPageSize())
                .fetch(this::mapToBook);
    }

    public Optional<Book> findById(UUID id) {
        return Optional.ofNullable(
                dsl.select(ID, TITLE, AUTHOR, GENRE, DESCRIPTION, PUBLISHED)
                        .from(BOOKS)
                        .where(ID.eq(id))
                        .fetchOne(this::mapToBook)
        );
    }

    public Book save(Book book) {
        return dsl.insertInto(BOOKS)
                .set(ID, book.getId())
                .set(TITLE, book.getTitle())
                .set(AUTHOR, book.getAuthor())
                .set(GENRE, book.getGenre().name())
                .set(DESCRIPTION, book.getDescription())
                .set(PUBLISHED, book.isPublished())
                .returning(ID, TITLE, AUTHOR, GENRE, DESCRIPTION, PUBLISHED)
                .fetchOne(this::mapToBook);
    }

    public Optional<Book> update(Book updatedBook) {
        int updatedRows = dsl.update(BOOKS)
                .set(TITLE, updatedBook.getTitle())
                .set(AUTHOR, updatedBook.getAuthor())
                .set(GENRE, updatedBook.getGenre().name())
                .set(DESCRIPTION, updatedBook.getDescription())
                .set(PUBLISHED, updatedBook.isPublished())
                .where(ID.eq(updatedBook.getId()))
                .execute();

        if (updatedRows == 0) {
            return Optional.empty();
        }

        return Optional.of(updatedBook);
    }

    public void delete(UUID id) {
        dsl.deleteFrom(BOOKS)
                .where(ID.eq(id))
                .execute();
    }

    public void deleteAll() {
        dsl.deleteFrom(BOOKS)
                .execute();
    }

    private Book mapToBook(org.jooq.Record record) {
        return new Book(
                record.get(ID),
                record.get(TITLE),
                record.get(AUTHOR),
                Genre.valueOf(record.get(GENRE)),
                record.get(DESCRIPTION),
                record.get(PUBLISHED)
        );
    }

    private List<? extends SortField<?>> buildSortFields(Pagination pagination) {
        return pagination.getSortOrders()
                .stream()
                .map(sortOrder -> {
                    var sortField = field(name(sortOrder.getField()));
                    return switch (sortOrder.getDirection()) {
                        case ASC -> sortField.asc();
                        case DESC -> sortField.desc();
                    };
                })
                .toList();
    }
}