package com.github.N1ckBaran0v.library.repository;

import com.github.N1ckBaran0v.library.data.Book;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface BooksRepository {
    List<Book> findAllBooks(@NotNull Connection connection) throws SQLException;
    List<Book> findBooksByIds(@NotNull Connection connection, @NotNull List<Long> ids) throws SQLException;
    void createBook(@NotNull Connection connection, @NotNull Book book) throws SQLException;
    Optional<Book> findBookById(@NotNull Connection connection, @NotNull Long id) throws SQLException;
    void updateBook(@NotNull Connection connection, @NotNull Book book) throws SQLException;
    void deleteBook(@NotNull Connection connection, @NotNull Long id) throws SQLException;
}
