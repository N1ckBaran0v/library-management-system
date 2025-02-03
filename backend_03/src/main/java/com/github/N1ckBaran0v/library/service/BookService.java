package com.github.N1ckBaran0v.library.service;

import com.github.N1ckBaran0v.library.component.SessionInfo;
import com.github.N1ckBaran0v.library.data.Book;
import com.github.N1ckBaran0v.library.data.History;
import com.github.N1ckBaran0v.library.form.SearchForm;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface BookService {
    List<Book> findBooks(@NotNull SessionInfo sessionInfo, @NotNull SearchForm searchForm);
    List<Book> findUserBooks(@NotNull SessionInfo sessionInfo, @NotNull String username);
    List<History> findUserHistory(@NotNull SessionInfo sessionInfo, @NotNull String username);
    void getBooks(@NotNull SessionInfo sessionInfo, @NotNull String username, @NotNull List<Long> books);
    void returnBooks(@NotNull SessionInfo sessionInfo, @NotNull String username, @NotNull List<Long> books);
    void createBook(@NotNull SessionInfo sessionInfo, @NotNull Book book);
    void updateBook(@NotNull SessionInfo sessionInfo, @NotNull Book book);
    void deleteBook(@NotNull SessionInfo sessionInfo, @NotNull Long book);
}
