package com.github.N1ckBaran0v.library.controller;

import com.github.N1ckBaran0v.library.server.EndpointContext;
import com.github.N1ckBaran0v.library.server.GsonConvertor;
import com.github.N1ckBaran0v.library.server.HttpStatus;
import com.github.N1ckBaran0v.library.server.JsonConvertor;
import com.github.N1ckBaran0v.library.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PostRegisterControllerTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private PostRegisterController controller;

    private EndpointContext ctx;

    private String body = "{\"username\":\"username\",\"password\":\"password\",\"confirmPassword\":\"password\",\"name\":\"name\",\"phone\":\"8(900) 000-00-00\",\"role\":\"user\"}";
    private final Map<String, String> requestParams = new HashMap<>();
    private InputStream requestBody = new ByteArrayInputStream(body.getBytes());
    private final JsonConvertor jsonConvertor = new GsonConvertor();
    private final String sessionId = "sessionId";

    @BeforeEach
    void setUp() {
        ctx = new EndpointContext(requestParams, requestBody, jsonConvertor, sessionId);
    }

    @Test
    void loginSuccess() {
        controller.handle(ctx);
        verify(userService).register(any(), any());
        assertEquals(HttpStatus.CREATED, ctx.getResponseCode());
        assertEquals("\"Success\"", ctx.getResponseBody());
    }

    @Test
    void formFailure() {
        body = "{\"username\":\"username\",\"password\":\"password\",\"confirmPassword\":\"no\",\"name\":\"name\",\"phone\":\"8(900) 000-00-00\",\"role\":\"user\"}";
        requestBody = new ByteArrayInputStream(body.getBytes());
        ctx = new EndpointContext(requestParams, requestBody, jsonConvertor, sessionId);
        controller.handle(ctx);
        verify(userService, never()).register(any(), any());
        assertEquals(HttpStatus.BAD_REQUEST, ctx.getResponseCode());
        assertEquals("\"400 Bad Request\"", ctx.getResponseBody());
    }

    @Test
    void dataFailure() {
        willThrow(ForbiddenException.class).given(userService).register(any(), any());
        controller.handle(ctx);
        assertEquals(HttpStatus.FORBIDDEN, ctx.getResponseCode());
        assertEquals("\"403 Forbidden\"", ctx.getResponseBody());
    }

    @Test
    void conflictFailure1() {
        willThrow(SessionNotEndedException.class).given(userService).register(any(), any());
        controller.handle(ctx);
        assertEquals(HttpStatus.CONFLICT, ctx.getResponseCode());
        assertEquals("\"409 Conflict\"", ctx.getResponseBody());
    }

    @Test
    void conflictFailure2() {
        willThrow(UserAlreadyExistsException.class).given(userService).register(any(), any());
        controller.handle(ctx);
        assertEquals(HttpStatus.CONFLICT, ctx.getResponseCode());
        assertEquals("\"409 Conflict\"", ctx.getResponseBody());
    }
}