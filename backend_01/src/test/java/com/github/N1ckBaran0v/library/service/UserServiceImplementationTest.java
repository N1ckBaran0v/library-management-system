package com.github.N1ckBaran0v.library.service;

import com.github.N1ckBaran0v.library.data.User;
import com.github.N1ckBaran0v.library.form.LoginForm;
import com.github.N1ckBaran0v.library.form.RegisterForm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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

    private Map<String, User> map;
    private User admin, worker, user;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        var field1 = UserServiceImplementation.class.getDeclaredField("users");
        field1.setAccessible(true);
        map = (Map<String, User>) field1.get(userService);
        var field2 = UserServiceImplementation.class.getDeclaredField("usersSessions");
        field2.setAccessible(true);
        var tmpMap = (Map<String, Set<String>>) field2.get(userService);
        admin = new User();
        admin.setUsername("adminUser");
        admin.setPassword("admin");
        admin.setName("admin");
        admin.setPhone("+8(900) 000-00-00");
        admin.setRole(User.ADMIN_ROLE);
        map.put("sessionAdmin", admin);
        var set1 = new HashSet<String>();
        set1.add("sessionAdmin");
        tmpMap.put(admin.getUsername(), set1);
        worker = new User();
        worker.setUsername("workerUser");
        worker.setPassword("worker");
        worker.setName("worker");
        worker.setPhone("+8(900) 000-00-01");
        worker.setRole(User.WORKER_ROLE);
        map.put("sessionWorker", worker);
        var set2 = new HashSet<String>();
        set2.add("sessionWorker");
        tmpMap.put(worker.getUsername(), set2);
        user = new User();
        user.setUsername("userUser");
        user.setPassword("user");
        user.setName("user");
        user.setPhone("+8(900) 000-00-02");
        user.setRole(User.USER_ROLE);
        map.put("sessionUser", user);
        var set3 = new HashSet<String>();
        set3.add("sessionUser");
        tmpMap.put(user.getUsername(), set3);
    }

    @Test
    void successfulRegistration1() {
        var form = new RegisterForm();
        form.setUsername("username");
        form.setPassword("password");
        form.setName("name");
        form.setPhone("+8(900) 000-00-03");
        form.setRole(User.USER_ROLE);
        userService.register(form, "sessionId");
        verify(databaseService).createUser(any());
    }

    @Test
    void successfulRegistration2() {
        var form = new RegisterForm();
        form.setUsername("username");
        form.setPassword("password");
        form.setName("name");
        form.setPhone("+8(900) 000-00-03");
        form.setRole(User.WORKER_ROLE);
        userService.register(form, "sessionAdmin");
        verify(databaseService).createUser(any());
    }

    @Test
    void registrationForbidden() {
        var form = new RegisterForm();
        form.setUsername("username");
        form.setPassword("password");
        form.setName("name");
        form.setPhone("+8(900) 000-00-03");
        form.setRole(User.WORKER_ROLE);
        assertThrows(ForbiddenException.class, () -> userService.register(form, "sessionId"));
        verify(databaseService, never()).createUser(any());
    }

    @Test
    void registrationSessionNotEnded() {
        var form = new RegisterForm();
        form.setUsername("username");
        form.setPassword("password");
        form.setName("name");
        form.setPhone("+8(900) 000-00-03");
        form.setRole(User.WORKER_ROLE);
        assertThrows(SessionNotEndedException.class, () -> userService.register(form, "sessionUser"));
        verify(databaseService, never()).createUser(any());
    }

    @Test
    void successfulLogin() {
        var form = new LoginForm();
        form.setUsername("username");
        form.setPassword("password");
        var user = new User();
        user.setUsername("username");
        user.setPassword("password");
        given(databaseService.findUserByUsername("username")).willReturn(user);
        userService.login(form, "sessionId");
        assertTrue(map.containsKey("sessionId"));
    }

    @Test
    void invalidLoginData1() {
        var form = new LoginForm();
        form.setUsername("username");
        form.setPassword("password");
        given(databaseService.findUserByUsername("username")).willReturn(new User());
        assertThrows(InvalidLoginDataException.class, () -> userService.login(form, "sessionId"));
        assertFalse(map.containsKey("sessionId"));
    }

    @Test
    void invalidLoginData2() {
        var form = new LoginForm();
        form.setUsername("username");
        form.setPassword("password");
        willThrow(UserNotFoundException.class).given(databaseService).findUserByUsername("username");
        assertThrows(InvalidLoginDataException.class, () -> userService.login(form, "sessionId"));
        assertFalse(map.containsKey("sessionId"));
    }

    @Test
    void loginSessionNotEnded() {
        var form = new LoginForm();
        form.setUsername("username");
        form.setPassword("password");
        assertThrows(SessionNotEndedException.class, () -> userService.login(form, "sessionUser"));
        verify(databaseService, never()).findUserByUsername(any());
    }

    @Test
    void successfulLogout() {
        userService.logout("sessionUser");
        assertFalse(map.containsKey("sessionUser"));
    }

    @Test
    void LogoutFailed() {
        assertThrows(UnauthorizedException.class, () -> userService.logout("sessionId"));
    }

    @Test
    void successfulDeleteUser() {
        userService.deleteUser("sessionAdmin", worker.getUsername());
        assertFalse(map.containsKey("sessionWorker"));
    }

    @Test
    void forbiddenDeleteUser() {
        assertThrows(ForbiddenException.class, () -> userService.deleteUser("sessionUser", admin.getUsername()));
        assertTrue(map.containsKey("sessionAdmin"));
    }

    @Test
    void successfulGetUser() {
        assertEquals(admin, userService.getUser("sessionAdmin"));
    }

    @Test
    void userNotFound() {
        assertThrows(UserNotFoundException.class, () -> userService.getUser("sessionId"));
    }

    @Test
    void successfulGetAnotherUser() {
        given(databaseService.findUserByUsername(worker.getUsername())).willReturn(worker);
        assertEquals(worker, userService.getUser("sessionAdmin", worker.getUsername()));
    }

    @Test
    void forbiddenGetAnotherUser() {
        assertThrows(ForbiddenException.class, () -> userService.getUser("sessionUser", worker.getUsername()));
        verify(databaseService, never()).findUserByUsername(any());
    }

    @Test
    void successfulGetUsername() {
        assertEquals(admin.getUsername(), userService.getUsername("sessionAdmin"));
    }
}