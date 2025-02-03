package com.github.N1ckBaran0v.library.controller;

import com.github.N1ckBaran0v.library.component.SessionInfo;
import com.github.N1ckBaran0v.library.form.LoginForm;
import com.github.N1ckBaran0v.library.service.InvalidLoginDataException;
import com.github.N1ckBaran0v.library.service.SessionNotEndedException;
import com.github.N1ckBaran0v.library.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class LoginControllerTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private LoginController loginController;

    @Mock
    private Errors errors;

    @Mock
    private SessionInfo sessionInfo;

    @Mock
    private LoginForm loginForm;

    @Test
    void loginForm() {
        var result = loginController.loginForm();
        assertNotNull(result);
    }

    @Test
    void loginSuccess() {
        given(errors.hasErrors()).willReturn(false);
        var result = loginController.login(loginForm, sessionInfo, errors);
        verify(userService).login(sessionInfo, loginForm);
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNull(result.getBody());
    }

    @Test
    void loginHasErrors() {
        given(errors.hasErrors()).willReturn(true);
        var result = loginController.login(loginForm, sessionInfo, errors);
        assertNotNull(result);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("Invalid login data", result.getBody());
    }

    @Test
    void loginInvalidData() {
        given(errors.hasErrors()).willReturn(false);
        willThrow(InvalidLoginDataException.class).given(userService).login(sessionInfo, loginForm);
        var result = loginController.login(loginForm, sessionInfo, errors);
        assertNotNull(result);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("Invalid login data", result.getBody());
    }

    @Test
    void loginSessionNotEnded() {
        given(errors.hasErrors()).willReturn(false);
        willThrow(SessionNotEndedException.class).given(userService).login(sessionInfo, loginForm);
        var result = loginController.login(loginForm, sessionInfo, errors);
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