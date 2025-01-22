package com.github.N1ckBaran0v.library.form;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LoginFormTest {
    @InjectMocks
    private LoginForm loginForm;

    @Test
    void hasNoErrors() {
        loginForm.setUsername("username");
        loginForm.setPassword("password");
        assertFalse(loginForm.hasErrors());
    }

    @Test
    void hasErrors() {
        assertTrue(loginForm.hasErrors());
    }
}