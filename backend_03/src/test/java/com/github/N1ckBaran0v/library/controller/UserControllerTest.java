package com.github.N1ckBaran0v.library.controller;

import com.github.N1ckBaran0v.library.component.SessionInfo;
import com.github.N1ckBaran0v.library.data.Book;
import com.github.N1ckBaran0v.library.data.History;
import com.github.N1ckBaran0v.library.data.User;
import com.github.N1ckBaran0v.library.service.BookService;
import com.github.N1ckBaran0v.library.service.UnauthorizedException;
import com.github.N1ckBaran0v.library.service.UserNotFoundException;
import com.github.N1ckBaran0v.library.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @Mock
    private UserService userService;

    @Mock
    private BookService bookService;

    @InjectMocks
    private UserController userController;

    @Mock
    private SessionInfo sessionInfo;

    @Test
    void historySuccess1() {
        var list = List.of(new History());
        given(bookService.findUserHistory(sessionInfo, "username")).willReturn(list);
        var result = userController.history("username", sessionInfo);
        verify(sessionInfo, never()).getUsername();
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(list, result.getBody());
    }

    @Test
    void historySuccess2() {
        var list = List.of(new History());
        given(sessionInfo.getUsername()).willReturn("username");
        given(bookService.findUserHistory(sessionInfo, "username")).willReturn(list);
        var result = userController.history(null, sessionInfo);
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(list, result.getBody());
    }

    @Test
    void historyUnauthorized() {
        given(sessionInfo.getUsername()).willReturn(null);
        assertThrows(UnauthorizedException.class, () -> userController.history(null, sessionInfo));
        verify(bookService, never()).findUserHistory(any(), any());
    }

    @Test
    void historyNotFound() {
        willThrow(UserNotFoundException.class).given(bookService).findUserHistory(sessionInfo, "username");
        var result = userController.history("username", sessionInfo);
        verify(sessionInfo, never()).getUsername();
        assertNotNull(result);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("User not found", result.getBody());
    }

    @Test
    void booksSuccess1() {
        var list = List.of(new Book());
        given(bookService.findUserBooks(sessionInfo, "username")).willReturn(list);
        var result = userController.books("username", sessionInfo);
        verify(sessionInfo, never()).getUsername();
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(list, result.getBody());
    }

    @Test
    void booksSuccess2() {
        var list = List.of(new Book());
        given(sessionInfo.getUsername()).willReturn("username");
        given(bookService.findUserBooks(sessionInfo, "username")).willReturn(list);
        var result = userController.books(null, sessionInfo);
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(list, result.getBody());
    }

    @Test
    void booksUnauthorized() {
        given(sessionInfo.getUsername()).willReturn(null);
        assertThrows(UnauthorizedException.class, () -> userController.books(null, sessionInfo));
        verify(bookService, never()).findUserHistory(any(), any());
    }

    @Test
    void booksNotFound() {
        willThrow(UserNotFoundException.class).given(bookService).findUserBooks(sessionInfo, "username");
        var result = userController.books("username", sessionInfo);
        verify(sessionInfo, never()).getUsername();
        assertNotNull(result);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("User not found", result.getBody());
    }

    @Test
    void infoSuccess1() {
        var user = new User();
        given(userService.getUser(sessionInfo, "username")).willReturn(user);
        var result = userController.info("username", sessionInfo);
        verify(sessionInfo, never()).getUsername();
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(user, result.getBody());
    }

    @Test
    void infoSuccess2() {
        var user = new User();
        given(userService.getUser(sessionInfo, "username")).willReturn(user);
        given(sessionInfo.getUsername()).willReturn("username");
        var result = userController.info(null, sessionInfo);
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(user, result.getBody());
    }

    @Test
    void infoUnauthorized() {
        given(sessionInfo.getUsername()).willReturn(null);
        assertThrows(UnauthorizedException.class, () -> userController.info(null, sessionInfo));
        verify(userService, never()).getUser(any(), any());
    }

    @Test
    void infoNotFound() {
        willThrow(UserNotFoundException.class).given(userService).getUser(any(), any());
        var result = userController.info("username", sessionInfo);
        verify(sessionInfo, never()).getUsername();
        assertNotNull(result);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("User not found", result.getBody());
    }

    @Test
    void deleteSuccess() {
        var result = userController.delete("username", sessionInfo);
        verify(userService).deleteUser(any(), any());
        assertNotNull(result);
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        assertNull(result.getBody());
    }

    @Test
    void deleteNotFound() {
        willThrow(UserNotFoundException.class).given(userService).deleteUser(any(), any());
        var result = userController.delete("username", sessionInfo);
        assertNotNull(result);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("User not found", result.getBody());
    }
}