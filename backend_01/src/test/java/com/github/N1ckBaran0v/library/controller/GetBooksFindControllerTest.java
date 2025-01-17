package com.github.N1ckBaran0v.library.controller;

import com.github.N1ckBaran0v.library.data.Book;
import com.github.N1ckBaran0v.library.data.User;
import com.github.N1ckBaran0v.library.server.EndpointContext;
import com.github.N1ckBaran0v.library.server.HttpStatus;
import com.github.N1ckBaran0v.library.service.BookService;
import com.github.N1ckBaran0v.library.service.ForbiddenException;
import com.github.N1ckBaran0v.library.service.UnauthorizedException;
import com.github.N1ckBaran0v.library.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class GetBooksFindControllerTest {
    @Mock
    private UserService userService;

    @Mock
    private BookService bookService;

    @InjectMocks
    private GetBooksFindController controller;

    @Mock
    private EndpointContext ctx;

    @BeforeEach
    void setUp() {
        given(ctx.getSessionId()).willReturn("sessionId");
    }

    @Test
    void searchSuccess() {
        var arr = new ArrayList<Book>();
        given(userService.getUsername("sessionId")).willReturn("username");
        given(userService.getUser("sessionId")).willReturn(new User());
        given(bookService.findBooks(any(), any())).willReturn(arr);
        controller.handle(ctx);
        verify(ctx).setResponseBody(arr);
    }

    @Test
    void authFailure() {
        willThrow(UnauthorizedException.class).given(userService).getUsername(any());
        controller.handle(ctx);
        verify(bookService, never()).findBooks(any(), any());
        verify(ctx).setResponseCode(HttpStatus.UNAUTHORIZED);
        verify(ctx).setResponseBody("401 Unauthorized");
    }

    @Test
    void forbiddenFailure() {
        given(userService.getUsername("sessionId")).willReturn("username");
        given(userService.getUser("sessionId")).willReturn(new User());
        willThrow(ForbiddenException.class).given(bookService).findBooks(any(), any());
        controller.handle(ctx);
        verify(ctx).setResponseCode(HttpStatus.FORBIDDEN);
        verify(ctx).setResponseBody("403 Forbidden");
    }
}