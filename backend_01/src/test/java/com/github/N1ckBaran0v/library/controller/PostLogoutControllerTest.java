package com.github.N1ckBaran0v.library.controller;

import com.github.N1ckBaran0v.library.server.EndpointContext;
import com.github.N1ckBaran0v.library.server.GsonConvertor;
import com.github.N1ckBaran0v.library.server.HttpStatus;
import com.github.N1ckBaran0v.library.server.JsonConvertor;
import com.github.N1ckBaran0v.library.service.UnauthorizedException;
import com.github.N1ckBaran0v.library.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostLogoutControllerTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private PostLogoutController controller;

    private EndpointContext ctx;

    private final Map<String, String> requestParams = new HashMap<>();
    private final InputStream requestBody = mock(InputStream.class);
    private final JsonConvertor jsonConvertor = new GsonConvertor();
    private final String sessionId = "sessionId";

    @BeforeEach
    void setUp() {
        ctx = new EndpointContext(requestParams, requestBody, jsonConvertor, sessionId);
    }

    @Test
    void logoutSuccess() {
        given(userService.getUsername(sessionId)).willReturn("username");
        controller.handle(ctx);
        verify(userService).logout("username");
        assertEquals(HttpStatus.OK, ctx.getResponseCode());
        assertEquals("\"Success\"", ctx.getResponseBody());
    }

    @Test
    void logoutFailure() {
        willThrow(UnauthorizedException.class).given(userService).getUsername(any());
        controller.handle(ctx);
        verify(userService, never()).logout(any());
        assertEquals(HttpStatus.UNAUTHORIZED, ctx.getResponseCode());
        assertEquals("\"401 Unauthorized\"", ctx.getResponseBody());
    }
}