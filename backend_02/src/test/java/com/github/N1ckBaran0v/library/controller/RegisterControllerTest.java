package com.github.N1ckBaran0v.library.controller;

import com.github.N1ckBaran0v.library.component.SessionInfo;
import com.github.N1ckBaran0v.library.form.RegisterForm;
import com.github.N1ckBaran0v.library.service.SessionNotEndedException;
import com.github.N1ckBaran0v.library.service.UserAlreadyExistsException;
import com.github.N1ckBaran0v.library.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RegisterControllerTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private RegisterController controller;

    private RegisterForm form;

    @Mock
    private SessionInfo sessionInfo;

    @BeforeEach
    void setUp() {
        form = new RegisterForm();
        form.setUsername("username");
        form.setPassword("password");
        form.setConfirmPassword("password");
        form.setName("name");
        form.setPhone("8(900) 000-00-00");
    }

    @Test
    void register() {
        var result = controller.register();
        assertNotNull(result);
    }

    @Test
    void registerSuccess() {
        var result = controller.register(form, sessionInfo);
        verify(userService).register(sessionInfo, form);
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNull(result.getBody());
    }

    @Test
    void registerHasErrors() {
        form.setName("");
        var result = controller.register(form, sessionInfo);
        verify(userService, never()).register(any(), any());
        assertNotNull(result);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("Invalid register data", result.getBody());
    }

    @Test
    void registerConfirmationError() {
        form.setConfirmPassword("another password");
        var result = controller.register(form, sessionInfo);
        verify(userService, never()).register(any(), any());
        assertNotNull(result);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("Invalid register data", result.getBody());
    }

    @Test
    void registerSessionNotEnded() {
        willThrow(SessionNotEndedException.class).given(userService).register(sessionInfo, form);
        var result = controller.register(form, sessionInfo);
        assertNotNull(result);
        assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
        assertEquals("Session not ended", result.getBody());
    }

    @Test
    void registerUserAlreadyExists() {
        willThrow(UserAlreadyExistsException.class).given(userService).register(sessionInfo, form);
        var result = controller.register(form, sessionInfo);
        assertNotNull(result);
        assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
        assertEquals("User already exists", result.getBody());
    }
}