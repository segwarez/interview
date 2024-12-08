package com.segwarez.vertxweb.repository;

import com.segwarez.vertxweb.service.Book;
import com.segwarez.vertxweb.service.Genre;
import com.segwarez.vertxweb.service.Pagination;
import io.vertx.core.Future;
import io.vertx.sqlclient.*;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.function.Function;

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

    public Future<List<Book>> findAll(Pagination pagination) {
        return dbClient
            .query(SQL_FIND_ALL + pagination.prepareQueryParams())
            .mapping(MAPPER)
            .execute()
            .map(rowSet -> {
                List<Book> books = new ArrayList<>();
                rowSet.forEach(books::add);
                return books;
            });
    }

    public Future<List<Book>> findByTitleContaining(String title, Pagination pagination) {
        return dbClient
            .preparedQuery(SQL_FIND_BY_TITLE_CONTAINING + pagination.prepareQueryParams())
            .mapping(MAPPER)
            .execute(Tuple.of("%" + title + "%"))
            .map(rowSet -> {
                List<Book> books = new ArrayList<>();
                rowSet.forEach(books::add);
                return books;
            });
    }

    public Future<List<Book>> findByPublished(boolean isPublished, Pagination pagination) {
        return dbClient
            .preparedQuery(SQL_FIND_BY_PUBLISHED + pagination.prepareQueryParams())
            .mapping(MAPPER)
            .execute(Tuple.of(isPublished))
            .map(rowSet -> {
                List<Book> books = new ArrayList<>();
                rowSet.forEach(books::add);
                return books;
            });
    }

    public Future<Book> findById(UUID id) {
        return dbClient
            .preparedQuery(SQL_FIND_BY_ID)
            .mapping(MAPPER)
            .execute(Tuple.of(id))
            .map(rowSet -> {
                RowIterator<Book> iterator = rowSet.iterator();
                if (iterator.hasNext()) {
                    return iterator.next();
                } else {
                    throw new NoSuchElementException();
                }
            });
    }

    public Future<Book> save(Book book) {
        return dbClient
            .preparedQuery(SQL_CREATE)
            .execute(Tuple.of(
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

    public Future<Book> update(Book updatedBook) {
        return dbClient.getConnection().compose(sqlConnection ->
            sqlConnection
                .preparedQuery(SQL_FIND_BY_ID)
                .mapping(MAPPER)
                .execute(Tuple.of(updatedBook.getId()))
                .map(rowSet -> {
                    RowIterator<Book> iterator = rowSet.iterator();
                    if (iterator.hasNext()) {
                        return iterator.next();
                    } else {
                        throw new NoSuchElementException();
                    }
                })
                .compose(book -> sqlConnection
                    .preparedQuery(SQL_UPDATE)
                    .execute(Tuple.of(
                        updatedBook.getId(),
                        updatedBook.getTitle(),
                        updatedBook.getAuthor(),
                        updatedBook.getGenre(),
                        updatedBook.getDescription(),
                        updatedBook.isPublished())
                    )
                    .map(rowSet -> {
                        if (rowSet.rowCount() == 1) {
                            return updatedBook;
                        } else {
                            throw new IllegalStateException();
                        }
                    })));
    }

    public Future<Void> delete(UUID id) {
        dbClient
            .preparedQuery(SQL_DELETE)
            .execute(Tuple.of(id))
            .map(SqlResult::rowCount);
        return Future.succeededFuture();
    }

    public Future<Void> deleteAll() {
        dbClient
            .query(SQL_DELETE_ALL)
            .execute();
        return Future.succeededFuture();
    }
}
