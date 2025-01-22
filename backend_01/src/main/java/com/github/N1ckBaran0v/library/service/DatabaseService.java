package com.github.N1ckBaran0v.library.service;

import com.github.N1ckBaran0v.library.data.Book;
import com.github.N1ckBaran0v.library.data.History;
import com.github.N1ckBaran0v.library.data.User;
import com.github.N1ckBaran0v.library.form.SearchForm;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface DatabaseService {
    User findUserByUsername(@NotNull String username);
    void deleteUserByUsername(@NotNull String username);
    void createUser(@NotNull User user);
    List<Book> findBooksByParams(@NotNull SearchForm searchForm);
    List<Book> findBooksByUsername(@NotNull String username);
    List<History> findHistoryByUsername(@NotNull String username);
    void getBooks(@NotNull String username, @NotNull List<Long> books);
    void returnBooks(@NotNull String username, @NotNull List<Long> books);
    void createBook(@NotNull Book book);
    void updateBook(@NotNull Book book);
    void deleteBook(@NotNull Long book);
}
