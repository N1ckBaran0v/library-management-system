package com.github.N1ckBaran0v.library.controller;

import com.github.N1ckBaran0v.library.data.Book;
import com.github.N1ckBaran0v.library.data.User;
import com.github.N1ckBaran0v.library.server.EndpointContext;
import com.github.N1ckBaran0v.library.server.HttpStatus;
import com.github.N1ckBaran0v.library.service.BookNotFoundException;
import com.github.N1ckBaran0v.library.service.BookService;
import com.github.N1ckBaran0v.library.service.UnauthorizedException;
import com.github.N1ckBaran0v.library.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PostBooksUpdateControllerTest {
    @Mock
    private UserService userService;

    @Mock
    private BookService bookService;

    @InjectMocks
    private PostBooksUpdateController controller;

    @Mock
    private EndpointContext ctx;

    @BeforeEach
    void setUp() {
        given(ctx.getSessionId()).willReturn("sessionId");
    }

    @Test
    void updateBookSuccess() {
        var user = new User();
        user.setRole(User.ADMIN_ROLE);
        given(userService.getUser("sessionId")).willReturn(user);
        var book = new Book();
        book.setId(1L);
        book.setTitle("title");
        book.setAuthor("author");
        book.setGenre("genre");
        book.setAvailableCount(5);
        book.setTotalCount(5);
        given(ctx.getRequestBody(Book.class)).willReturn(book);
        controller.handle(ctx);
        verify(bookService).updateBook(book, user);
        verify(ctx).setResponseCode(HttpStatus.ACCEPTED);
        verify(ctx).setResponseBody("202 Accepted");
    }

    @Test
    void authFailure() {
        willThrow(UnauthorizedException.class).given(userService).getUser(any());
        controller.handle(ctx);
        verify(bookService, never()).updateBook(any(), any());
        verify(ctx).setResponseCode(HttpStatus.UNAUTHORIZED);
        verify(ctx).setResponseBody("401 Unauthorized");
    }

    @Test
    void permissionsFailure() {
        var user = new User();
        given(userService.getUser("sessionId")).willReturn(user);
        controller.handle(ctx);
        verify(bookService, never()).updateBook(any(), any());
        verify(ctx).setResponseCode(HttpStatus.FORBIDDEN);
        verify(ctx).setResponseBody("403 Forbidden");
    }

    @Test
    void validationFailure() {
        var user = new User();
        user.setRole(User.ADMIN_ROLE);
        given(userService.getUser("sessionId")).willReturn(user);
        var book = new Book();
        book.setTitle("title");
        book.setAuthor("author");
        book.setGenre("genre");
        book.setAvailableCount(5);
        book.setTotalCount(5);
        given(ctx.getRequestBody(Book.class)).willReturn(book);
        controller.handle(ctx);
        verify(bookService, never()).updateBook(any(), any());
        verify(ctx).setResponseCode(HttpStatus.BAD_REQUEST);
        verify(ctx).setResponseBody("400 Bad Request");
    }

    @Test
    void notFoundFailure() {
        var user = new User();
        user.setRole(User.ADMIN_ROLE);
        given(userService.getUser("sessionId")).willReturn(user);
        var book = new Book();
        book.setId(1L);
        book.setTitle("title");
        book.setAuthor("author");
        book.setGenre("genre");
        book.setAvailableCount(5);
        book.setTotalCount(5);
        given(ctx.getRequestBody(Book.class)).willReturn(book);
        willThrow(BookNotFoundException.class).given(bookService).updateBook(book, user);
        controller.handle(ctx);
        verify(ctx).setResponseCode(HttpStatus.BAD_REQUEST);
        verify(ctx).setResponseBody("400 Bad Request");
    }
}