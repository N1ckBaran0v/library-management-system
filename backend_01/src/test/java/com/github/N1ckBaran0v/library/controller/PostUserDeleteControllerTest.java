package com.github.N1ckBaran0v.library.controller;

import com.github.N1ckBaran0v.library.data.User;
import com.github.N1ckBaran0v.library.server.EndpointContext;
import com.github.N1ckBaran0v.library.server.HttpStatus;
import com.github.N1ckBaran0v.library.service.*;
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
class PostUserDeleteControllerTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private PostUserDeleteController controller;

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
        var map = new HashMap<String, String>();
        map.put("username", "username");
        given(ctx.getRequestParams()).willReturn(map);
        controller.handle(ctx);
        verify(userService).deleteUser("sessionId", "username");
        verify(ctx).setResponseCode(HttpStatus.NO_CONTENT);
        verify(ctx).setResponseBody("204 No Content");
    }

    @Test
    void authFailure() {
        willThrow(UnauthorizedException.class).given(userService).getUser(any());
        controller.handle(ctx);
        verify(userService, never()).deleteUser(any(), any());
        verify(ctx).setResponseCode(HttpStatus.UNAUTHORIZED);
        verify(ctx).setResponseBody("401 Unauthorized");
    }

    @Test
    void permissionsFailure() {
        var user = new User();
        given(userService.getUser("sessionId")).willReturn(user);
        controller.handle(ctx);
        verify(userService, never()).deleteUser(any(), any());
        verify(ctx).setResponseCode(HttpStatus.FORBIDDEN);
        verify(ctx).setResponseBody("403 Forbidden");
    }

    @Test
    void notFoundFailure() {
        var user = new User();
        user.setRole(User.ADMIN_ROLE);
        given(userService.getUser("sessionId")).willReturn(user);
        var map = new HashMap<String, String>();
        map.put("username", "username");
        given(ctx.getRequestParams()).willReturn(map);
        willThrow(UserNotFoundException.class).given(userService).deleteUser("sessionId", "username");
        controller.handle(ctx);
        verify(ctx).setResponseCode(HttpStatus.BAD_REQUEST);
        verify(ctx).setResponseBody("400 Bad Request");
    }

    @Test
    void paramFailure() {
        var user = new User();
        user.setRole(User.ADMIN_ROLE);
        given(userService.getUser("sessionId")).willReturn(user);
        var map = new HashMap<String, String>();
        given(ctx.getRequestParams()).willReturn(map);
        controller.handle(ctx);
        verify(userService, never()).deleteUser(any(), any());
        verify(ctx).setResponseCode(HttpStatus.BAD_REQUEST);
        verify(ctx).setResponseBody("400 Bad Request");
    }
}