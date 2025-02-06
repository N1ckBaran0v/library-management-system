package com.github.N1ckBaran0v.library.service;

import com.github.N1ckBaran0v.library.component.SessionInfo;
import com.github.N1ckBaran0v.library.data.Book;
import com.github.N1ckBaran0v.library.data.History;
import com.github.N1ckBaran0v.library.data.User;
import com.github.N1ckBaran0v.library.form.SearchForm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BookServiceImplementationTest {
    @Mock
    private DatabaseService databaseService;

    @InjectMocks
    private BookServiceImplementation bookService;

    private SessionInfo sessionInfo;

    @BeforeEach
    void setUp() {
        sessionInfo = new SessionInfo();
        sessionInfo.setRole(User.USER_ROLE);
        sessionInfo.setUsername("ClownVasya");
    }

    @Test
    void findBooksSuccess1() {
        var list = List.of(new Book());
        var params = new SearchForm();
        given(databaseService.findBooksByParams(params)).willReturn(list);
        assertEquals(list, bookService.findBooks(sessionInfo, params));
    }

    @Test
    void findBooksSuccess2() {
        var list = List.of(new Book());
        var params = new SearchForm();
        params.setShowUnavailable(true);
        sessionInfo.setRole(User.ADMIN_ROLE);
        given(databaseService.findBooksByParams(params)).willReturn(list);
        assertEquals(list, bookService.findBooks(sessionInfo, params));
    }

    @Test
    void findBooksUnauthorized() {
        sessionInfo.setRole(User.UNAUTHORIZED);
        assertThrows(UnauthorizedException.class, () -> bookService.findBooks(sessionInfo, new SearchForm()));
        verify(databaseService, never()).findBooksByParams(any());
    }

    @Test
    void findBooksForbidden() {
        var params = new SearchForm();
        params.setShowUnavailable(true);
        assertThrows(ForbiddenException.class, () -> bookService.findBooks(sessionInfo, params));
        verify(databaseService, never()).findBooksByParams(any());
    }

    @Test
    void findUserBooksSuccess1() {
        var list = List.of(new Book());
        given(databaseService.findBooksByUsername(any())).willReturn(list);
        assertEquals(list, bookService.findUserBooks(sessionInfo, "ClownVasya"));
    }

    @Test
    void findUserBooksSuccess2() {
        var list = List.of(new Book());
        sessionInfo.setRole(User.ADMIN_ROLE);
        given(databaseService.findBooksByUsername(any())).willReturn(list);
        assertEquals(list, bookService.findUserBooks(sessionInfo, "ClownPetya"));
    }

    @Test
    void findUserBooksUnauthorized() {
        sessionInfo.setRole(User.UNAUTHORIZED);
        assertThrows(UnauthorizedException.class, () -> bookService.findUserBooks(sessionInfo, "ClownVasya"));
        verify(databaseService, never()).findBooksByUsername(any());
    }

    @Test
    void findUserBooksForbidden() {
        assertThrows(ForbiddenException.class, () -> bookService.findUserBooks(sessionInfo, "ClownPetya"));
        verify(databaseService, never()).findBooksByUsername(any());
    }

    @Test
    void findUserHistorySuccess1() {
        var list = List.of(new History());
        given(databaseService.findHistoryByUsername(any())).willReturn(list);
        assertEquals(list, bookService.findUserHistory(sessionInfo, "ClownVasya"));
    }

    @Test
    void findUserHistorySuccess2() {
        var list = List.of(new History());
        sessionInfo.setRole(User.ADMIN_ROLE);
        given(databaseService.findHistoryByUsername(any())).willReturn(list);
        assertEquals(list, bookService.findUserHistory(sessionInfo, "ClownPetya"));
    }

    @Test
    void findUserHistoryUnauthorized() {
        sessionInfo.setRole(User.UNAUTHORIZED);
        assertThrows(UnauthorizedException.class, () -> bookService.findUserHistory(sessionInfo, "ClownVasya"));
        verify(databaseService, never()).findHistoryByUsername(any());
    }

    @Test
    void findUserHistoryForbidden() {
        assertThrows(ForbiddenException.class, () -> bookService.findUserHistory(sessionInfo, "ClownPetya"));
        verify(databaseService, never()).findHistoryByUsername(any());
    }

    @Test
    void getBooksSuccess() {
        var books = List.of(1L, 2L);
        sessionInfo.setRole(User.WORKER_ROLE);
        bookService.getBooks(sessionInfo, "user", books);
        verify(databaseService).getBooks("user", books);
    }

    @Test
    void getBooksUnauthorized() {
        sessionInfo.setRole(User.UNAUTHORIZED);
        assertThrows(UnauthorizedException.class, () -> bookService.getBooks(sessionInfo, "user", List.of(1L, 2L)));
        verify(databaseService, never()).getBooks(any(), any());
    }

    @Test
    void getBooksForbidden() {
        assertThrows(ForbiddenException.class, () -> bookService.getBooks(sessionInfo, "user", List.of(1L, 2L)));
        verify(databaseService, never()).getBooks(any(), any());
    }


    @Test
    void returnBooks() {
        var books = List.of(1L, 2L);
        sessionInfo.setRole(User.WORKER_ROLE);
        bookService.returnBooks(sessionInfo, "user", books);
        verify(databaseService).returnBooks("user", books);
    }

    @Test
    void returnBooksUnauthorized() {
        sessionInfo.setRole(User.UNAUTHORIZED);
        assertThrows(UnauthorizedException.class, () -> bookService.returnBooks(sessionInfo, "user", List.of(1L, 2L)));
        verify(databaseService, never()).returnBooks(any(), any());
    }

    @Test
    void returnBooksForbidden() {
        assertThrows(ForbiddenException.class, () -> bookService.returnBooks(sessionInfo, "user", List.of(1L, 2L)));
        verify(databaseService, never()).returnBooks(any(), any());
    }

    @Test
    void createBookSuccess() {
        var book = new Book();
        sessionInfo.setRole(User.ADMIN_ROLE);
        bookService.createBook(sessionInfo, book);
        verify(databaseService).createBook(book);
    }

    @Test
    void createBookUnauthorized() {
        sessionInfo.setRole(User.UNAUTHORIZED);
        assertThrows(UnauthorizedException.class, () -> bookService.createBook(sessionInfo, new Book()));
        verify(databaseService, never()).createBook(any());
    }

    @Test
    void createBookForbidden() {
        assertThrows(ForbiddenException.class, () -> bookService.createBook(sessionInfo, new Book()));
        verify(databaseService, never()).createBook(any());
    }

    @Test
    void updateBook() {
        var book = new Book();
        sessionInfo.setRole(User.ADMIN_ROLE);
        bookService.updateBook(sessionInfo, book);
        verify(databaseService).updateBook(book);
    }

    @Test
    void updateBookUnauthorized() {
        sessionInfo.setRole(User.UNAUTHORIZED);
        assertThrows(UnauthorizedException.class, () -> bookService.updateBook(sessionInfo, new Book()));
        verify(databaseService, never()).updateBook(any());
    }

    @Test
    void updateBookForbidden() {
        assertThrows(ForbiddenException.class, () -> bookService.updateBook(sessionInfo, new Book()));
        verify(databaseService, never()).updateBook(any());
    }

    @Test
    void deleteBookSuccess() {
        sessionInfo.setRole(User.ADMIN_ROLE);
        bookService.deleteBook(sessionInfo, 1L);
        verify(databaseService).deleteBook(1L);
    }

    @Test
    void deleteBookUnauthorized() {
        sessionInfo.setRole(User.UNAUTHORIZED);
        assertThrows(UnauthorizedException.class, () -> bookService.deleteBook(sessionInfo, 1L));
        verify(databaseService, never()).deleteBook(any());
    }

    @Test
    void deleteBookForbidden() {
        assertThrows(ForbiddenException.class, () -> bookService.deleteBook(sessionInfo, 1L));
        verify(databaseService, never()).deleteBook(any());
    }
}