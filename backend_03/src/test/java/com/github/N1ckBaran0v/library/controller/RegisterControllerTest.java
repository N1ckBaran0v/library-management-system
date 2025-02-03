package com.github.N1ckBaran0v.library.controller;

import com.github.N1ckBaran0v.library.component.SessionInfo;
import com.github.N1ckBaran0v.library.form.RegisterForm;
import com.github.N1ckBaran0v.library.service.SessionNotEndedException;
import com.github.N1ckBaran0v.library.service.UserAlreadyExistsException;
import com.github.N1ckBaran0v.library.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;

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

    @Mock
    private RegisterForm form;

    @Mock
    private SessionInfo sessionInfo;

    @Mock
    private Errors errors;

    @Test
    void register() {
        var result = controller.register();
        assertNotNull(result);
    }

    @Test
    void registerSuccess() {
        given(errors.hasErrors()).willReturn(false);
        given(form.getPassword()).willReturn("password");
        given(form.getConfirmPassword()).willReturn("password");
        var result = controller.register(form, sessionInfo, errors);
        verify(userService).register(sessionInfo, form);
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNull(result.getBody());
    }

    @Test
    void registerHasErrors() {
        given(errors.hasErrors()).willReturn(true);
        var result = controller.register(form, sessionInfo, errors);
        verify(form, never()).getPassword();
        verify(form, never()).getConfirmPassword();
        verify(userService, never()).register(any(), any());
        assertNotNull(result);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("Invalid register data", result.getBody());
    }

    @Test
    void registerConfirmationError() {
        given(errors.hasErrors()).willReturn(false);
        given(form.getPassword()).willReturn("password");
        given(form.getConfirmPassword()).willReturn("passw0rd");
        var result = controller.register(form, sessionInfo, errors);
        verify(userService, never()).register(any(), any());
        assertNotNull(result);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("Invalid register data", result.getBody());
    }

    @Test
    void registerSessionNotEnded() {
        given(errors.hasErrors()).willReturn(false);
        given(form.getPassword()).willReturn("password");
        given(form.getConfirmPassword()).willReturn("password");
        willThrow(SessionNotEndedException.class).given(userService).register(sessionInfo, form);
        var result = controller.register(form, sessionInfo, errors);
        assertNotNull(result);
        assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
        assertEquals("Session not ended", result.getBody());
    }

    @Test
    void registerUserAlreadyExists() {
        given(errors.hasErrors()).willReturn(false);
        given(form.getPassword()).willReturn("password");
        given(form.getConfirmPassword()).willReturn("password");
        willThrow(UserAlreadyExistsException.class).given(userService).register(sessionInfo, form);
        var result = controller.register(form, sessionInfo, errors);
        assertNotNull(result);
        assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
        assertEquals("User already exists", result.getBody());
    }
}