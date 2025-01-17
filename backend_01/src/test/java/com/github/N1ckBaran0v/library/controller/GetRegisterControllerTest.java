package com.github.N1ckBaran0v.library.controller;

import com.github.N1ckBaran0v.library.server.EndpointContext;
import com.github.N1ckBaran0v.library.server.GsonConvertor;
import com.github.N1ckBaran0v.library.server.HttpStatus;
import com.github.N1ckBaran0v.library.server.JsonConvertor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class GetRegisterControllerTest {
    @InjectMocks
    private GetRegisterController controller;

    private EndpointContext ctx;

    private final Map<String, String> requestParams = new HashMap<>();
    private final InputStream requestBody = mock(InputStream.class);
    private final JsonConvertor jsonConvertor = new GsonConvertor();

    @BeforeEach
    void setUp() {
        ctx = new EndpointContext(requestParams, requestBody, jsonConvertor, "");
    }

    @Test
    void handle() {
        var answer = "{\"username\":\"\",\"password\":\"\",\"confirmPassword\":\"\",\"name\":\"\",\"phone\":\"\",\"role\":\"user\"}";
        controller.handle(ctx);
        assertEquals(answer, ctx.getResponseBody());
        assertEquals(HttpStatus.OK, ctx.getResponseCode());
    }
}