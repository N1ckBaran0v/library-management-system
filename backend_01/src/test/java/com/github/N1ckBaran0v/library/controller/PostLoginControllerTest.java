package com.github.N1ckBaran0v.library.controller;

import com.github.N1ckBaran0v.library.form.LoginForm;
import com.github.N1ckBaran0v.library.server.EndpointContext;
import com.github.N1ckBaran0v.library.server.GsonConvertor;
import com.github.N1ckBaran0v.library.server.HttpStatus;
import com.github.N1ckBaran0v.library.server.JsonConvertor;
import com.github.N1ckBaran0v.library.service.InvalidLoginDataException;
import com.github.N1ckBaran0v.library.service.SessionNotEndedException;
import com.github.N1ckBaran0v.library.service.UserService;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostLoginControllerTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private PostLoginController controller;

    private EndpointContext ctx;

    private String body = "{\"username\":\"username\",\"password\":\"password\"}";
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
        verify(userService).login(any(), any());
        assertEquals(HttpStatus.OK, ctx.getResponseCode());
        assertEquals("\"Success\"", ctx.getResponseBody());
    }

    @Test
    void formFailure() {
        body = "{\"username\":\"\",\"password\":\"\"}";
        requestBody = new ByteArrayInputStream(body.getBytes());
        ctx = new EndpointContext(requestParams, requestBody, jsonConvertor, sessionId);
        controller.handle(ctx);
        verify(userService, never()).login(any(), any());
        assertEquals(HttpStatus.BAD_REQUEST, ctx.getResponseCode());
        assertEquals("\"400 Bad Request\"", ctx.getResponseBody());
    }

    @Test
    void dataFailure() {
        willThrow(InvalidLoginDataException.class).given(userService).login(any(), any());
        controller.handle(ctx);
        assertEquals(HttpStatus.BAD_REQUEST, ctx.getResponseCode());
        assertEquals("\"400 Bad Request\"", ctx.getResponseBody());
    }

    @Test
    void conflictFailure() {
        willThrow(SessionNotEndedException.class).given(userService).login(any(), any());
        controller.handle(ctx);
        assertEquals(HttpStatus.CONFLICT, ctx.getResponseCode());
        assertEquals("\"409 Conflict\"", ctx.getResponseBody());
    }
}