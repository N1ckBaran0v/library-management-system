package com.github.N1ckBaran0v.library.repository;

import com.github.N1ckBaran0v.library.data.Book;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SQLiteBooksRepository implements BooksRepository {
    @Override
    public List<Book> findAllBooks(@NotNull Connection connection) throws SQLException {
        try (var statement = connection.prepareStatement("SELECT * FROM books;")) {
            return getBooks(statement.executeQuery());
        }
    }

    @Override
    public List<Book> findBooksByIds(@NotNull Connection connection, @NotNull List<Long> ids) throws SQLException {
        try (var statement = connection.prepareStatement("SELECT * FROM books WHERE book_id IN (:ids);")) {
            return getBooks(statement.executeQuery());
        }
    }

    private List<Book> getBooks(@NotNull ResultSet resultSet) throws SQLException {
        var answer = new ArrayList<Book>();
        while (resultSet.next()) {
            answer.add(getBook(resultSet));
        }
        return answer;
    }

    private Book getBook(@NotNull ResultSet resultSet) throws SQLException {
        var book = new Book();
        book.setId(resultSet.getLong("id"));
        book.setTitle(resultSet.getString("title"));
        book.setAuthor(resultSet.getString("author"));
        book.setGenre(resultSet.getString("genre"));
        book.setTotalCount(resultSet.getInt("total_count"));
        book.setAvailableCount(resultSet.getInt("available_count"));
        return book;
    }

    @Override
    public void createBook(@NotNull Connection connection, @NotNull Book book) throws SQLException {
        try (var statement = connection.prepareStatement("INSERT INTO books (title, author, genre, total_count, available_count) VALUES (?, ?, ?, ?, ?);")) {
            statement.setString(1, book.getTitle());
            statement.setString(2, book.getAuthor());
            statement.setString(3, book.getGenre());
            statement.setInt(4, book.getTotalCount());
            statement.setInt(5, book.getAvailableCount());
            statement.executeUpdate();
        }
    }

    @Override
    public Optional<Book> findBookById(@NotNull Connection connection, @NotNull Long id) throws SQLException {
        try (var statement = connection.prepareStatement("SELECT * FROM books WHERE book_id = (?);")) {
            statement.setLong(1, id);
            var resultSet = statement.executeQuery();
            var list = new ArrayList<Book>();
            if (resultSet.next()) {
                list.add(getBook(resultSet));
            }
            return Optional.ofNullable(list.isEmpty() ? null : list.getFirst());
        }
    }

    @Override
    public void updateBook(@NotNull Connection connection, @NotNull Book book) throws SQLException {
        try (var statement = connection.prepareStatement("UPDATE books SET title = (?), author = (?), genre = (?), total_count = (?), available_count = (?) WHERE book_id = (?);")) {
            statement.setString(1, book.getTitle());
            statement.setString(2, book.getAuthor());
            statement.setString(3, book.getGenre());
            statement.setInt(4, book.getTotalCount());
            statement.setInt(5, book.getAvailableCount());
            statement.setLong(6, book.getId());
            statement.executeUpdate();
        }
    }

    @Override
    public void deleteBook(@NotNull Connection connection, @NotNull Long id) throws SQLException {
        try (var statement = connection.prepareStatement("DELETE FROM books WHERE book_id = (?);")) {
            statement.setLong(1, id);
            statement.execute();
        }
    }
}
