package com.github.N1ckBaran0v.library.controller;

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
class PostBooksDeleteControllerTest {
    @Mock
    private UserService userService;

    @Mock
    private BookService bookService;

    @InjectMocks
    private PostBooksDeleteController controller;

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
        given(ctx.getRequestBody(Long.class)).willReturn(1L);
        controller.handle(ctx);
        verify(bookService).deleteBook(1L, user);
        verify(ctx).setResponseCode(HttpStatus.NO_CONTENT);
        verify(ctx).setResponseBody("204 No Content");
    }

    @Test
    void authFailure() {
        willThrow(UnauthorizedException.class).given(userService).getUser(any());
        controller.handle(ctx);
        verify(bookService, never()).deleteBook(any(), any());
        verify(ctx).setResponseCode(HttpStatus.UNAUTHORIZED);
        verify(ctx).setResponseBody("401 Unauthorized");
    }

    @Test
    void permissionsFailure() {
        var user = new User();
        given(userService.getUser("sessionId")).willReturn(user);
        controller.handle(ctx);
        verify(bookService, never()).deleteBook(any(), any());
        verify(ctx).setResponseCode(HttpStatus.FORBIDDEN);
        verify(ctx).setResponseBody("403 Forbidden");
    }

    @Test
    void notFoundFailure() {
        var user = new User();
        user.setRole(User.ADMIN_ROLE);
        given(userService.getUser("sessionId")).willReturn(user);
        given(ctx.getRequestBody(Long.class)).willReturn(1L);
        willThrow(BookNotFoundException.class).given(bookService).deleteBook(1L, user);
        controller.handle(ctx);
        verify(ctx).setResponseCode(HttpStatus.BAD_REQUEST);
        verify(ctx).setResponseBody("400 Bad Request");
    }
}