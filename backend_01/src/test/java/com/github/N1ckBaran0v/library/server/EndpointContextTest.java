package com.github.N1ckBaran0v.library.server;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willThrow;

@ExtendWith(MockitoExtension.class)
class EndpointContextTest {
    @Mock
    private JsonConvertor jsonConvertor;

    @InjectMocks
    private EndpointContext ctx;

    @Test
    void getRequestBodyFail() throws IOException {
        willThrow(ConvertationException.class).given(jsonConvertor).fromJson(any(), any());
        assertThrows(ConvertationException.class, () -> ctx.getRequestBody(String.class));
    }
}