package com.github.N1ckBaran0v.library.service;

import com.github.N1ckBaran0v.library.data.Book;
import com.github.N1ckBaran0v.library.data.History;
import com.github.N1ckBaran0v.library.data.User;
import com.github.N1ckBaran0v.library.form.SearchForm;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
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

    @Test
    void successfulFindBooks() {
        var list = new ArrayList<Book>();
        given(databaseService.findBooksByParams(any())).willReturn(list);
        assertEquals(list, bookService.findBooks(new SearchForm(), new User()));
    }

    @Test
    void successfulFindUnavailableBooks() {
        var list = new ArrayList<Book>();
        given(databaseService.findBooksByParams(any())).willReturn(list);
        var user = new User();
        user.setRole(User.ADMIN_ROLE);
        var form = new SearchForm();
        form.setShowUnavailable(true);
        assertEquals(list, bookService.findBooks(form, user));
    }

    @Test
    void forbiddenFindBooks() {
        var user = new User();
        var form = new SearchForm();
        form.setShowUnavailable(true);
        assertThrows(ForbiddenException.class, () -> bookService.findBooks(form, user));
        verify(databaseService, never()).findBooksByParams(any());
    }

    @Test
    void successfulFindUserBooks1() {
        var list = new ArrayList<Book>();
        var user = new User();
        user.setRole(User.ADMIN_ROLE);
        given(databaseService.findBooksByUsername("username")).willReturn(list);
        assertEquals(list, bookService.findUserBooks("username", user));
    }

    @Test
    void successfulFindUserHBooks2() {
        var list = new ArrayList<Book>();
        var user = new User();
        user.setUsername("username");
        given(databaseService.findBooksByUsername("username")).willReturn(list);
        assertEquals(list, bookService.findUserBooks("username", user));
    }

    @Test
    void forbiddenFindUserBooks() {
        var user = new User();
        assertThrows(ForbiddenException.class, () -> bookService.findUserBooks("username", user));
        verify(databaseService, never()).findBooksByUsername(any());
    }

    @Test
    void successfulFindUserHistory1() {
        var list = new ArrayList<History>();
        var user = new User();
        user.setRole(User.ADMIN_ROLE);
        given(databaseService.findHistoryByUsername("username")).willReturn(list);
        assertEquals(list, bookService.findUserHistory("username", user));
    }

    @Test
    void successfulFindUserHistory2() {
        var list = new ArrayList<History>();
        var user = new User();
        user.setUsername("username");
        given(databaseService.findHistoryByUsername("username")).willReturn(list);
        assertEquals(list, bookService.findUserHistory("username", user));
    }

    @Test
    void forbiddenFindUserHistory() {
        var user = new User();
        assertThrows(ForbiddenException.class, () -> bookService.findUserHistory("username", user));
        verify(databaseService, never()).findHistoryByUsername(any());
    }

    @Test
    void successfulGetBooks() {
        var books = List.of(1L);
        var user = new User();
        user.setRole(User.WORKER_ROLE);
        bookService.getBooks("username", books, user);
        verify(databaseService).getBooks("username", books);
    }

    @Test
    void forbiddenGetBooks() {
        var books = List.of(1L);
        var user = new User();
        assertThrows(ForbiddenException.class, () -> bookService.getBooks("username", books, user));
        verify(databaseService, never()).getBooks(any(), any());
    }

    @Test
    void successfulReturnBooks() {
        var books = List.of(1L);
        var user = new User();
        user.setRole(User.WORKER_ROLE);
        bookService.returnBooks("username", books, user);
        verify(databaseService).returnBooks("username", books);
    }

    @Test
    void forbiddenReturnBooks() {
        var books = List.of(1L);
        var user = new User();
        assertThrows(ForbiddenException.class, () -> bookService.returnBooks("username", books, user));
        verify(databaseService, never()).returnBooks(any(), any());
    }

    @Test
    void successfulCreateBook() {
        var book = new Book();
        var user = new User();
        user.setRole(User.ADMIN_ROLE);
        bookService.createBook(book, user);
        verify(databaseService).createBook(book);
    }

    @Test
    void forbiddenCreateBook() {
        var book = new Book();
        var user = new User();
        assertThrows(ForbiddenException.class, () -> bookService.createBook(book, user));
        verify(databaseService, never()).createBook(any());
    }

    @Test
    void successfulUpdateBook() {
        var book = new Book();
        var user = new User();
        user.setRole(User.ADMIN_ROLE);
        bookService.updateBook(book, user);
        verify(databaseService).updateBook(book);
    }

    @Test
    void forbiddenUpdateBook() {
        var book = new Book();
        var user = new User();
        assertThrows(ForbiddenException.class, () -> bookService.updateBook(book, user));
        verify(databaseService, never()).updateBook(any());
    }

    @Test
    void successfulDeleteBook() {
        var user = new User();
        user.setRole(User.ADMIN_ROLE);
        bookService.deleteBook(1L, user);
        verify(databaseService).deleteBook(1L);
    }

    @Test
    void forbiddenDeleteBook() {
        var user = new User();
        assertThrows(ForbiddenException.class, () -> bookService.deleteBook(1L, user));
        verify(databaseService, never()).deleteBook(any());
    }
}