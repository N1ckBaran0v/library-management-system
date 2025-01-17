package com.github.N1ckBaran0v.library.service;

import com.github.N1ckBaran0v.library.data.Book;
import com.github.N1ckBaran0v.library.data.History;
import com.github.N1ckBaran0v.library.data.User;
import com.github.N1ckBaran0v.library.form.SearchForm;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface BookService {
    List<Book> findBooks(@NotNull SearchForm searchForm, @NotNull User user);
    List<Book> findUserBooks(@NotNull String username, @NotNull User user);
    List<History> findUserHistory(@NotNull String username, @NotNull User user);
}
