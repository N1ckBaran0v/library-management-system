package com.github.N1ckBaran0v.library.server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

// Assertions only for arrays and primitive types, because i'm not a gson developer:)
class GsonConvertorTest {
    private JsonConvertor convertor;

    @BeforeEach
    void setUp() {
        convertor = new GsonConvertor();
    }

    @Test
    void toJsonNumberSuccess() {
        var answer = "123";
        var result = convertor.toJson(BigDecimal.valueOf(123));
        assertEquals(answer, result);
    }

    @Test
    void fromJsonNumberSuccess() throws IOException {
        var answer = BigDecimal.valueOf(123);
        var result = convertor.fromJson(new ByteArrayInputStream("123".getBytes()), BigDecimal.class);
        assertEquals(answer, result);
    }

    @Test
    void toJsonArraySuccess() {
        var answer = "[1,2,3]";
        var result = convertor.toJson(new int[]{1, 2, 3});
        assertEquals(answer, result);
    }

    @Test
    void fromJsonArraySuccess() throws IOException {
        var answer = new int[]{1, 2, 3};
        var result = convertor.fromJson(new ByteArrayInputStream("[1,2,3]".getBytes()), int[].class);
        assertArrayEquals(answer, result);
    }
}