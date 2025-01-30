package com.github.N1ckBaran0v.library.service;

import com.github.N1ckBaran0v.library.component.SessionInfo;
import com.github.N1ckBaran0v.library.data.Book;
import com.github.N1ckBaran0v.library.data.History;
import com.github.N1ckBaran0v.library.data.User;
import com.github.N1ckBaran0v.library.form.SearchForm;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImplementation implements BookService {
    private final DatabaseService databaseService;

    @Autowired
    public BookServiceImplementation(@NotNull DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    @Override
    public List<Book> findBooks(@NotNull SessionInfo sessionInfo, @NotNull SearchForm searchForm) {
        if (searchForm.isShowUnavailable() && !User.ADMIN_ROLE.equals(sessionInfo.getRole())) {
            throw new ForbiddenException();
        }
        return databaseService.findBooksByParams(searchForm);
    }

    @Override
    public List<Book> findUserBooks(@NotNull SessionInfo sessionInfo, @NotNull String username) {
        if (!(User.ADMIN_ROLE.equals(sessionInfo.getRole()) || username.equals(sessionInfo.getUsername()))) {
            throw new ForbiddenException();
        }
        return databaseService.findBooksByUsername(username);
    }

    @Override
    public List<History> findUserHistory(@NotNull SessionInfo sessionInfo, @NotNull String username) {
        if (!(User.ADMIN_ROLE.equals(sessionInfo.getRole()) || username.equals(sessionInfo.getUsername()))) {
            throw new ForbiddenException();
        }
        return databaseService.findHistoryByUsername(username);
    }

    @Override
    public void getBooks(@NotNull SessionInfo sessionInfo, @NotNull String username, @NotNull List<Long> books) {
        if (!(User.ADMIN_ROLE.equals(sessionInfo.getRole()) || User.WORKER_ROLE.equals(sessionInfo.getRole()))) {
            throw new ForbiddenException();
        }
        databaseService.getBooks(username, books);
    }

    @Override
    public void returnBooks(@NotNull SessionInfo sessionInfo, @NotNull String username, @NotNull List<Long> books) {
        if (!(User.ADMIN_ROLE.equals(sessionInfo.getRole()) || User.WORKER_ROLE.equals(sessionInfo.getRole()))) {
            throw new ForbiddenException();
        }
        databaseService.returnBooks(username, books);
    }

    @Override
    public void createBook(@NotNull SessionInfo sessionInfo, @NotNull Book book) {
        if (!(User.ADMIN_ROLE.equals(sessionInfo.getRole()) || User.WORKER_ROLE.equals(sessionInfo.getRole()))) {
            throw new ForbiddenException();
        }
        databaseService.createBook(book);
    }

    @Override
    public void updateBook(@NotNull SessionInfo sessionInfo, @NotNull Book book) {
        if (!(User.ADMIN_ROLE.equals(sessionInfo.getRole()) || User.WORKER_ROLE.equals(sessionInfo.getRole()))) {
            throw new ForbiddenException();
        }
        databaseService.updateBook(book);
    }

    @Override
    public void deleteBook(@NotNull SessionInfo sessionInfo, @NotNull Long book) {
        if (!(User.ADMIN_ROLE.equals(sessionInfo.getRole()) || User.WORKER_ROLE.equals(sessionInfo.getRole()))) {
            throw new ForbiddenException();
        }
        databaseService.deleteBook(book);
    }
}
