package com.github.N1ckBaran0v.library.controller;

import com.github.N1ckBaran0v.library.component.SessionInfo;
import com.github.N1ckBaran0v.library.data.Book;
import com.github.N1ckBaran0v.library.form.SearchForm;
import com.github.N1ckBaran0v.library.service.BookNotFoundException;
import com.github.N1ckBaran0v.library.service.BookService;
import com.github.N1ckBaran0v.library.service.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BooksControllerTest {
    @Mock
    private BookService bookService;

    @InjectMocks
    private BooksController booksController;

    @Mock
    private SessionInfo sessionInfo;

    @Test
    void find() {
        var books = List.of(new Book());
        var form = new SearchForm();
        given(bookService.findBooks(sessionInfo, form)).willReturn(books);
        var result = booksController.find(form, sessionInfo);
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(books, result.getBody());
    }

    @Test
    void create() {
        var book = new Book();
        var result = booksController.create(book, sessionInfo);
        verify(bookService).createBook(sessionInfo, book);
        assertNotNull(result);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertNull(result.getBody());
    }

    @Test
    void updateSuccess() {
        var book = new Book();
        var result = booksController.update(book, sessionInfo);
        verify(bookService).updateBook(sessionInfo, book);
        assertNotNull(result);
        assertEquals(HttpStatus.ACCEPTED, result.getStatusCode());
        assertNull(result.getBody());
    }

    @Test
    void updateNotFound() {
        willThrow(BookNotFoundException.class).given(bookService).updateBook(any(), any());
        var result = booksController.update(new Book(), sessionInfo);
        assertNotNull(result);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("Book not found", result.getBody());
    }

    @Test
    void deleteSuccess() {
        var result = booksController.delete(1L, sessionInfo);
        verify(bookService).deleteBook(sessionInfo, 1L);
        assertNotNull(result);
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        assertNull(result.getBody());
    }

    @Test
    void deleteNotFound() {
        willThrow(BookNotFoundException.class).given(bookService).deleteBook(any(), any());
        var result = booksController.delete(1L, sessionInfo);
        assertNotNull(result);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("Book not found", result.getBody());
    }

    @Test
    void getBooksSuccess() {
        var books = List.of(1L, 2L);
        var result = booksController.getBooks(books, "username", sessionInfo);
        verify(bookService).getBooks(sessionInfo, "username", books);
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNull(result.getBody());
    }

    @Test
    void getBooksNotFound1() {
        willThrow(UserNotFoundException.class).given(bookService).getBooks(any(), any(), any());
        var result = booksController.getBooks(List.of(1L, 2L), "username", sessionInfo);
        assertNotNull(result);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("User not found", result.getBody());
    }

    @Test
    void getBooksNotFound2() {
        willThrow(BookNotFoundException.class).given(bookService).getBooks(any(), any(), any());
        var result = booksController.getBooks(List.of(1L, 2L), "username", sessionInfo);
        assertNotNull(result);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("Book not found", result.getBody());
    }

    @Test
    void returnBooksSuccess() {
        var books = List.of(1L, 2L);
        var result = booksController.returnBooks(books, "username", sessionInfo);
        verify(bookService).returnBooks(sessionInfo, "username", books);
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNull(result.getBody());
    }

    @Test
    void returnBooksNotFound1() {
        willThrow(UserNotFoundException.class).given(bookService).returnBooks(any(), any(), any());
        var result = booksController.returnBooks(List.of(1L, 2L), "username", sessionInfo);
        assertNotNull(result);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("User not found", result.getBody());
    }

    @Test
    void returnBooksNotFound2() {
        willThrow(BookNotFoundException.class).given(bookService).returnBooks(any(), any(), any());
        var result = booksController.returnBooks(List.of(1L, 2L), "username", sessionInfo);
        assertNotNull(result);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("Book not found", result.getBody());
    }
}