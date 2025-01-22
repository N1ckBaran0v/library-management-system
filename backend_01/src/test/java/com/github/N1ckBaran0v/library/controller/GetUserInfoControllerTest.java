package com.github.N1ckBaran0v.library.controller;

import com.github.N1ckBaran0v.library.data.User;
import com.github.N1ckBaran0v.library.server.EndpointContext;
import com.github.N1ckBaran0v.library.server.HttpStatus;
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
class GetUserInfoControllerTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private GetUserInfoController controller;

    @Mock
    private EndpointContext ctx;

    @BeforeEach
    void setUp() {
        given(ctx.getSessionId()).willReturn("sessionId");
    }

    @Test
    void searchSelfSuccess() {
        var user = new User();
        given(ctx.getRequestParams()).willReturn(new HashMap<>());
        given(userService.getUsername("sessionId")).willReturn("username");
        given(userService.getUser("sessionId", "username")).willReturn(user);
        controller.handle(ctx);
        verify(ctx).setResponseBody(user);
    }

    @Test
    void searchAnotherSuccess() {
        var map = new HashMap<String, String>();
        map.put("username", "anotherUsername");
        var user = new User();
        given(ctx.getRequestParams()).willReturn(map);
        given(userService.getUsername("sessionId")).willReturn("username");
        given(userService.getUser("sessionId", "anotherUsername")).willReturn(user);
        controller.handle(ctx);
        verify(ctx).setResponseBody(user);
    }

    @Test
    void authFailure() {
        willThrow(UnauthorizedException.class).given(userService).getUsername(any());
        controller.handle(ctx);
        verify(userService, never()).getUser(any(), any());
        verify(ctx).setResponseCode(HttpStatus.UNAUTHORIZED);
        verify(ctx).setResponseBody("401 Unauthorized");
    }

    @Test
    void forbiddenFailure() {
        given(ctx.getRequestParams()).willReturn(new HashMap<>());
        given(userService.getUsername("sessionId")).willReturn("username");
        willThrow(ForbiddenException.class).given(userService).getUser(any(), any());
        controller.handle(ctx);
        verify(ctx).setResponseCode(HttpStatus.FORBIDDEN);
        verify(ctx).setResponseBody("403 Forbidden");
    }
}