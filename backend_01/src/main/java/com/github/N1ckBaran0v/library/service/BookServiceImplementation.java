package com.github.N1ckBaran0v.library.service;

import com.github.N1ckBaran0v.library.data.Book;
import com.github.N1ckBaran0v.library.data.History;
import com.github.N1ckBaran0v.library.data.User;
import com.github.N1ckBaran0v.library.form.SearchForm;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BookServiceImplementation implements BookService {
    private final DatabaseService databaseService;

    public BookServiceImplementation(@NotNull DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    @Override
    public List<Book> findBooks(@NotNull SearchForm searchForm, @NotNull User user) {
        if (searchForm.isShowUnavailable() && !User.ADMIN_ROLE.equals(user.getRole())) {
            throw new ForbiddenException();
        }
        return databaseService.findBooksByParams(searchForm);
    }

    @Override
    public List<Book> findUserBooks(@NotNull String username, @NotNull User user) {
        if (!(User.ADMIN_ROLE.equals(user.getRole()) || username.equals(user.getUsername()))) {
            throw new ForbiddenException();
        }
        return databaseService.findBooksByUsername(username);
    }

    @Override
    public List<History> findUserHistory(@NotNull String username, @NotNull User user) {
        if (!(User.ADMIN_ROLE.equals(user.getRole()) || username.equals(user.getUsername()))) {
            throw new ForbiddenException();
        }
        return databaseService.findHistoryByUsername(username);
    }

    @Override
    public void getBooks(@NotNull String username, @NotNull List<Long> books, @NotNull User user) {
        if (!(User.ADMIN_ROLE.equals(user.getRole()) || User.WORKER_ROLE.equals(user.getRole()))) {
            throw new ForbiddenException();
        }
        databaseService.getBooks(username, books);
    }

    @Override
    public void returnBooks(@NotNull String username, @NotNull List<Long> books, @NotNull User user) {
        if (!(User.ADMIN_ROLE.equals(user.getRole()) || User.WORKER_ROLE.equals(user.getRole()))) {
            throw new ForbiddenException();
        }
        databaseService.returnBooks(username, books);
    }

    @Override
    public void createBook(@NotNull Book book, @NotNull User user) {
        if (!(User.ADMIN_ROLE.equals(user.getRole()) || User.WORKER_ROLE.equals(user.getRole()))) {
            throw new ForbiddenException();
        }
        databaseService.createBook(book);
    }

    @Override
    public void updateBook(@NotNull Book book, @NotNull User user) {
        if (!(User.ADMIN_ROLE.equals(user.getRole()) || User.WORKER_ROLE.equals(user.getRole()))) {
            throw new ForbiddenException();
        }
        databaseService.updateBook(book);
    }

    @Override
    public void deleteBook(@NotNull Long book, @NotNull User user) {
        if (!(User.ADMIN_ROLE.equals(user.getRole()) || User.WORKER_ROLE.equals(user.getRole()))) {
            throw new ForbiddenException();
        }
        databaseService.deleteBook(book);
    }
}
