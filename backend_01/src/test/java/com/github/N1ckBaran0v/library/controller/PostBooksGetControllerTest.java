package com.github.N1ckBaran0v.library.controller;

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

import java.util.HashMap;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PostBooksGetControllerTest {
    @Mock
    private UserService userService;

    @Mock
    private BookService bookService;

    @InjectMocks
    private PostBooksGetController controller;

    @Mock
    private EndpointContext ctx;

    @BeforeEach
    void setUp() {
        given(ctx.getSessionId()).willReturn("sessionId");
    }

    @Test
    void getBooksSuccess() {
        var arr = new Long[]{1L, 2L, 3L};
        var map = new HashMap<String, String>();
        map.put("username", "anotherUsername");
        given(ctx.getRequestBody(Long[].class)).willReturn(arr);
        given(ctx.getRequestParams()).willReturn(map);
        given(userService.getUsername("sessionId")).willReturn("username");
        given(userService.getUser("sessionId")).willReturn(new User());
        controller.handle(ctx);
        verify(bookService).getBooks(any(), any(), any());
    }

    @Test
    void authFailure() {
        willThrow(UnauthorizedException.class).given(userService).getUsername(any());
        controller.handle(ctx);
        verify(bookService, never()).getBooks(any(), any(), any());
        verify(ctx).setResponseCode(HttpStatus.UNAUTHORIZED);
        verify(ctx).setResponseBody("401 Unauthorized");
    }

    @Test
    void forbiddenFailure() {
        var arr = new Long[]{1L, 2L, 3L};
        var map = new HashMap<String, String>();
        map.put("username", "anotherUsername");
        given(ctx.getRequestBody(Long[].class)).willReturn(arr);
        given(ctx.getRequestParams()).willReturn(map);
        given(userService.getUsername("sessionId")).willReturn("username");
        given(userService.getUser("sessionId")).willReturn(new User());
        willThrow(ForbiddenException.class).given(bookService).getBooks(any(), any(), any());
        controller.handle(ctx);
        verify(ctx).setResponseCode(HttpStatus.FORBIDDEN);
        verify(ctx).setResponseBody("403 Forbidden");
    }

    @Test
    void noUserFailure() {
        var map = new HashMap<String, String>();
        given(ctx.getRequestParams()).willReturn(map);
        controller.handle(ctx);
        verify(bookService, never()).getBooks(any(), any(), any());
        verify(ctx).setResponseCode(HttpStatus.BAD_REQUEST);
        verify(ctx).setResponseBody("400 Bad Request");
    }

    @Test
    void noBookFailure() {
        var arr = new Long[]{};
        var map = new HashMap<String, String>();
        map.put("username", "anotherUsername");
        given(ctx.getRequestBody(Long[].class)).willReturn(arr);
        given(ctx.getRequestParams()).willReturn(map);
        controller.handle(ctx);
        verify(bookService, never()).getBooks(any(), any(), any());
        verify(ctx).setResponseCode(HttpStatus.BAD_REQUEST);
        verify(ctx).setResponseBody("400 Bad Request");
    }
}