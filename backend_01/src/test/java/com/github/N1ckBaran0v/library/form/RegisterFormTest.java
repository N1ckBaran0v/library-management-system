package com.github.N1ckBaran0v.library.form;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RegisterFormTest {
    @InjectMocks
    private RegisterForm form;

    @BeforeEach
    void setUp() {
        form.setUsername("username");
        form.setPassword("password");
        form.setConfirmPassword("password");
        form.setName("name");
        form.setPhone("8(900) 000-00-00");
    }

    @Test
    void hasNoErrors() {
        assertFalse(form.hasErrors());
    }

    @Test
    void invalidLogin() {
        form.setUsername("user");
        assertTrue(form.hasErrors());
    }

    @Test
    void blankName() {
        form.setName("");
        assertTrue(form.hasErrors());
    }

    @Test
    void invalidPassword() {
        form.setPassword("not a password");
        assertTrue(form.hasErrors());
    }
}