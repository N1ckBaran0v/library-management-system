package com.github.N1ckBaran0v.library.controller;

import com.github.N1ckBaran0v.library.component.SessionInfo;
import com.github.N1ckBaran0v.library.form.LoginForm;
import com.github.N1ckBaran0v.library.service.InvalidLoginDataException;
import com.github.N1ckBaran0v.library.service.SessionNotEndedException;
import com.github.N1ckBaran0v.library.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class LoginControllerTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private LoginController loginController;

    @Mock
    private SessionInfo sessionInfo;

    private LoginForm loginForm;

    @BeforeEach
    void setUp() {
        loginForm = new LoginForm();
        loginForm.setUsername("username");
        loginForm.setPassword("password");
    }

    @Test
    void loginForm() {
        var result = loginController.loginForm();
        assertNotNull(result);
    }

    @Test
    void loginSuccess() {
        var result = loginController.login(loginForm, sessionInfo);
        verify(userService).login(sessionInfo, loginForm);
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNull(result.getBody());
    }

    @Test
    void loginHasErrors() {
        loginForm.setPassword("");
        var result = loginController.login(loginForm, sessionInfo);
        assertNotNull(result);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("Invalid login data", result.getBody());
    }

    @Test
    void loginInvalidData() {
        willThrow(InvalidLoginDataException.class).given(userService).login(sessionInfo, loginForm);
        var result = loginController.login(loginForm, sessionInfo);
        assertNotNull(result);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("Invalid login data", result.getBody());
    }

    @Test
    void loginSessionNotEnded() {
        willThrow(SessionNotEndedException.class).given(userService).login(sessionInfo, loginForm);
        var result = loginController.login(loginForm, sessionInfo);
        assertNotNull(result);
        assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
        assertEquals("Session not ended", result.getBody());
    }

    @Test
    void logout() {
        var result = loginController.logout(sessionInfo);
        verify(userService).logout(sessionInfo);
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNull(result.getBody());
    }
}