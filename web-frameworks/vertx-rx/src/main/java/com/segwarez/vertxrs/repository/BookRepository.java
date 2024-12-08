package com.segwarez.vertxrs.repository;

import com.segwarez.vertxrs.service.Book;
import com.segwarez.vertxrs.service.Genre;
import com.segwarez.vertxrs.service.Pagination;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import io.vertx.rxjava3.sqlclient.*;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.function.Function;
import java.util.stream.StreamSupport;

@RequiredArgsConstructor
public class BookRepository {
    private static final String SQL_FIND_ALL = "SELECT * FROM books";
    private static final String SQL_FIND_BY_TITLE_CONTAINING = "SELECT * FROM books WHERE title LIKE $1";
    private static final String SQL_FIND_BY_PUBLISHED = "SELECT * FROM books WHERE published = $1";
    private static final String SQL_FIND_BY_ID = "SELECT * FROM books WHERE id = $1";
    private static final String SQL_CREATE = "INSERT INTO books (id, title, author, genre, description, published) " + "VALUES ($1, $2, $3, $4, $5, $6)";
    private static final String SQL_UPDATE = "UPDATE books SET title = $2, author = $3, genre = $4, " + "description = $5, published = $6 WHERE id = $1";
    private static final String SQL_DELETE = "DELETE FROM books WHERE id = $1";
    private static final String SQL_DELETE_ALL = "DELETE FROM books";
    private static final Function<Row, Book> MAPPER = row ->
        new Book(
            row.getUUID("id"),
            row.getString("title"),
            row.getString("author"),
            row.get(Genre.class, "genre"),
            row.getString("description"),
            row.getBoolean("published")
        );
    private final Pool dbClient;

    public Flowable<Book> findAll(Pagination pagination) {
        return dbClient
            .query(SQL_FIND_ALL + pagination.prepareQueryParams())
            .rxExecute()
            .flattenAsFlowable(
                rows -> StreamSupport.stream(rows.spliterator(), false)
                    .map(MAPPER)
                    .toList()
            );
    }

    public Flowable<Book> findByTitleContaining(String title, Pagination pagination) {
        return dbClient
            .preparedQuery(SQL_FIND_BY_TITLE_CONTAINING + pagination.prepareQueryParams())
            .rxExecute(Tuple.of("%" + title + "%"))
            .flattenAsFlowable(
                rows -> StreamSupport.stream(rows.spliterator(), false)
                    .map(MAPPER)
                    .toList()
            );
    }

    public Flowable<Book> findByPublished(boolean isPublished, Pagination pagination) {
        return dbClient
            .preparedQuery(SQL_FIND_BY_PUBLISHED + pagination.prepareQueryParams())
            .rxExecute(Tuple.of(isPublished))
            .flattenAsFlowable(
                rows -> StreamSupport.stream(rows.spliterator(), false)
                    .map(MAPPER)
                    .toList()
            );
    }

    public Single<Book> findById(UUID id) {
        return dbClient
            .preparedQuery(SQL_FIND_BY_ID)
            .mapping(MAPPER)
            .rxExecute(Tuple.of(id))
            .map(RowSet::iterator)
            .flatMap(iterator -> iterator.hasNext() ? Single.just(iterator.next()) : Single.error(new NoSuchElementException()));
    }

    public Single<Book> save(Book book) {
        return dbClient
            .preparedQuery(SQL_CREATE)
            .rxExecute(Tuple.of(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getGenre(),
                book.getDescription(),
                book.isPublished())
            )
            .map(rowSet -> {
                if (rowSet.rowCount() == 1) {
                    return book;
                } else {
                    throw new IllegalStateException();
                }
            });
    }

    public Maybe<Book> update(Book updatedBook) {
        return dbClient.withConnection(sqlConnection ->
            sqlConnection
                .preparedQuery(SQL_FIND_BY_ID)
                .mapping(MAPPER)
                .rxExecute(Tuple.of(updatedBook.getId()))
                .flatMap(rowSet -> rowSet.iterator().hasNext() ? Single.just(rowSet.iterator().next()) : Single.error(new NoSuchElementException()))
                .flatMap(book -> sqlConnection
                    .preparedQuery(SQL_UPDATE)
                    .mapping(MAPPER)
                    .rxExecute(Tuple.of(
                        updatedBook.getId(),
                        updatedBook.getTitle(),
                        updatedBook.getAuthor(),
                        updatedBook.getGenre(),
                        updatedBook.getDescription(),
                        updatedBook.isPublished())
                    ).map(rowSet -> {
                        if (rowSet.rowCount() == 1) {
                            return updatedBook;
                        } else {
                            throw new IllegalStateException();
                        }
                    })
                ).toMaybe()
        );
    }

    public Single<Integer> delete(UUID id) {
        return dbClient
            .preparedQuery(SQL_DELETE)
            .rxExecute(Tuple.of(id))
            .map(SqlResult::rowCount);
    }

    public Single<Integer> deleteAll() {
        return dbClient.
            query(SQL_DELETE_ALL)
            .rxExecute()
            .map(SqlResult::rowCount);
    }
}
