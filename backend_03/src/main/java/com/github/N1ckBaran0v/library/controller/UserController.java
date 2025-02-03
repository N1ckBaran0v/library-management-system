package com.github.N1ckBaran0v.library.controller;

import com.github.N1ckBaran0v.library.component.SessionInfo;
import com.github.N1ckBaran0v.library.service.BookService;
import com.github.N1ckBaran0v.library.service.UnauthorizedException;
import com.github.N1ckBaran0v.library.service.UserNotFoundException;
import com.github.N1ckBaran0v.library.service.UserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final BookService bookService;

    @Autowired
    public UserController(@NotNull UserService userService, @NotNull BookService bookService) {
        this.userService = userService;
        this.bookService = bookService;
    }

    @GetMapping("/history")
    public ResponseEntity<?> history(@RequestParam(required = false) String username, @NotNull SessionInfo sessionInfo) {
        ResponseEntity<?> response;
        try {
            if (username == null) {
                username = sessionInfo.getUsername();
                if (username == null) {
                    throw new UnauthorizedException();
                }
            } else {
                userService.getUser(sessionInfo, username);
            }
            response = ResponseEntity.ok(bookService.findUserHistory(sessionInfo, username));
        } catch (UserNotFoundException e) {
            response = ResponseEntity.badRequest().body("User not found");
        }
        return response;
    }

    @GetMapping("/books")
    public ResponseEntity<?> books(@RequestParam(required = false) String username, @NotNull SessionInfo sessionInfo) {
        ResponseEntity<?> response;
        try {
            if (username == null) {
                username = sessionInfo.getUsername();
                if (username == null) {
                    throw new UnauthorizedException();
                }
            } else {
                userService.getUser(sessionInfo, username);
            }
            response = ResponseEntity.ok(bookService.findUserBooks(sessionInfo, username));
        } catch (UserNotFoundException e) {
            response = ResponseEntity.badRequest().body("User not found");
        }
        return response;
    }

    @GetMapping("/info")
    public ResponseEntity<?> info(@RequestParam(required = false) String username, @NotNull SessionInfo sessionInfo) {
        ResponseEntity<?> response;
        try {
            if (username == null) {
                username = sessionInfo.getUsername();
                if (username == null) {
                    throw new UnauthorizedException();
                }
            } else {
                userService.getUser(sessionInfo, username);
            }
            response = ResponseEntity.ok(userService.getUser(sessionInfo, username));
        } catch (UserNotFoundException e) {
            response = ResponseEntity.badRequest().body("User not found");
        }
        return response;
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestParam String username, @NotNull SessionInfo sessionInfo) {
        ResponseEntity<?> response;
        try {
            userService.deleteUser(sessionInfo, username);
            response = ResponseEntity.noContent().build();
        } catch (UserNotFoundException e) {
            response = ResponseEntity.badRequest().body("User not found");
        }
        return response;
    }
}
