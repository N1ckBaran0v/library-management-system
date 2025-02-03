package com.github.N1ckBaran0v.library.service;

import com.github.N1ckBaran0v.library.component.SessionInfo;
import com.github.N1ckBaran0v.library.data.User;
import com.github.N1ckBaran0v.library.form.LoginForm;
import com.github.N1ckBaran0v.library.form.RegisterForm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceImplementationTest {
    @Mock
    private DatabaseService databaseService;

    @InjectMocks
    private UserServiceImplementation userService;

    private SessionInfo sessionInfo;

    @BeforeEach
    void setUp() {
        sessionInfo = new SessionInfo();
    }

    @Test
    void registerSuccess1() {
        var form = new RegisterForm();
        form.setUsername("username");
        userService.register(sessionInfo, form);
        verify(databaseService).createUser(any());
        assertEquals("username", sessionInfo.getUsername());
        assertEquals(User.USER_ROLE, sessionInfo.getRole());
    }

    @Test
    void registerSuccess2() {
        var form = new RegisterForm();
        form.setUsername("username");
        form.setRole(User.WORKER_ROLE);
        sessionInfo.setRole(User.ADMIN_ROLE);
        sessionInfo.setUsername("admin-user");
        userService.register(sessionInfo, form);
        verify(databaseService).createUser(any());
        assertEquals("admin-user", sessionInfo.getUsername());
        assertEquals(User.ADMIN_ROLE, sessionInfo.getRole());
    }

    @Test
    void registerSessionNotEnded() {
        var form = new RegisterForm();
        form.setUsername("username");
        sessionInfo.setUsername("user-user");
        sessionInfo.setRole(User.USER_ROLE);
        assertThrows(SessionNotEndedException.class, () -> userService.register(sessionInfo, form));
        verify(databaseService, never()).createUser(any());
        assertEquals("user-user", sessionInfo.getUsername());
        assertEquals(User.USER_ROLE, sessionInfo.getRole());
    }

    @Test
    void registerForbidden() {
        var form = new RegisterForm();
        form.setUsername("username");
        form.setRole(User.ADMIN_ROLE);
        assertThrows(ForbiddenException.class, () -> userService.register(sessionInfo, form));
        verify(databaseService, never()).createUser(any());
        assertNull(sessionInfo.getUsername());
        assertEquals(User.UNAUTHORIZED, sessionInfo.getRole());
    }

    @Test
    void loginSuccessful() {
        var form = new LoginForm();
        form.setUsername("username");
        form.setPassword("password");
        var user = new User();
        user.setUsername("username");
        user.setPassword("password");
        user.setRole(User.USER_ROLE);
        given(databaseService.findUserByUsername("username")).willReturn(user);
        userService.login(sessionInfo, form);
        assertEquals("username", sessionInfo.getUsername());
        assertEquals(User.USER_ROLE, sessionInfo.getRole());
    }

    @Test
    void loginSessionNotEnded() {
        sessionInfo.setUsername("username");
        sessionInfo.setRole(User.USER_ROLE);
        assertThrows(SessionNotEndedException.class, () -> userService.login(sessionInfo, new LoginForm()));
        verify(databaseService, never()).findUserByUsername(any());
        assertEquals("username", sessionInfo.getUsername());
        assertEquals(User.USER_ROLE, sessionInfo.getRole());
    }

    @Test
    void loginInvalid1() {
        var form = new LoginForm();
        form.setUsername("username");
        form.setPassword("password");
        var user = new User();
        user.setUsername("username");
        user.setPassword("another-password");
        given(databaseService.findUserByUsername("username")).willReturn(user);
        assertThrows(InvalidLoginDataException.class, () -> userService.login(sessionInfo, form));
        assertNull(sessionInfo.getUsername());
        assertEquals(User.UNAUTHORIZED, sessionInfo.getRole());
    }

    @Test
    void loginInvalid2() {
        var form = new LoginForm();
        form.setUsername("username");
        form.setPassword("password");
        willThrow(UserNotFoundException.class).given(databaseService).findUserByUsername(any());
        assertThrows(InvalidLoginDataException.class, () -> userService.login(sessionInfo, form));
        assertNull(sessionInfo.getUsername());
        assertEquals(User.UNAUTHORIZED, sessionInfo.getRole());
    }

    @Test
    void logoutSuccess() {
        sessionInfo.setRole(User.USER_ROLE);
        sessionInfo.setUsername("username");
        userService.logout(sessionInfo);
        assertNull(sessionInfo.getUsername());
        assertEquals(User.UNAUTHORIZED, sessionInfo.getRole());
    }

    @Test
    void logoutUnauthorized() {
        assertThrows(UnauthorizedException.class, () -> userService.logout(sessionInfo));
        assertNull(sessionInfo.getUsername());
        assertEquals(User.UNAUTHORIZED, sessionInfo.getRole());
    }

    @Test
    void deleteUserSuccess() {
        sessionInfo.setRole(User.ADMIN_ROLE);
        userService.deleteUser(sessionInfo, "username");
        verify(databaseService).deleteUserByUsername("username");
    }

    @Test
    void deleteUserUnauthorized() {
        assertThrows(UnauthorizedException.class, () -> userService.deleteUser(sessionInfo, "username"));
        verify(databaseService, never()).deleteUserByUsername(any());
    }

    @Test
    void deleteUserForbidden() {
        sessionInfo.setRole(User.USER_ROLE);
        assertThrows(ForbiddenException.class, () -> userService.deleteUser(sessionInfo, "username"));
        verify(databaseService, never()).deleteUserByUsername(any());
    }

    @Test
    void getUserSuccess() {
        sessionInfo.setRole(User.USER_ROLE);
        sessionInfo.setUsername("username");
        var user = new User();
        user.setUsername("username");
        given(databaseService.findUserByUsername("username")).willReturn(user);
        assertEquals(user, userService.getUser(sessionInfo));
    }

    @Test
    void getUserUnauthorized() {
        assertThrows(UnauthorizedException.class, () -> userService.getUser(sessionInfo, "username"));
        verify(databaseService, never()).findUserByUsername(any());
    }

    @Test
    void getUserSuccess1() {
        sessionInfo.setRole(User.ADMIN_ROLE);
        var user = new User();
        user.setUsername("username");
        given(databaseService.findUserByUsername("username")).willReturn(user);
        assertEquals(user, userService.getUser(sessionInfo, "username"));
    }

    @Test
    void getUserSuccess2() {
        sessionInfo.setRole(User.USER_ROLE);
        sessionInfo.setUsername("username");
        var user = new User();
        user.setUsername("username");
        given(databaseService.findUserByUsername("username")).willReturn(user);
        assertEquals(user, userService.getUser(sessionInfo, "username"));
    }

    @Test
    void getUserUnauthorized1() {
        assertThrows(UnauthorizedException.class, () -> userService.getUser(sessionInfo, "username"));
        verify(databaseService, never()).findUserByUsername(any());
    }

    @Test
    void getUserForbidden() {
        sessionInfo.setRole(User.USER_ROLE);
        sessionInfo.setUsername("username");
        assertThrows(ForbiddenException.class, () -> userService.getUser(sessionInfo, "user-user"));
        verify(databaseService, never()).findUserByUsername(any());
    }
}