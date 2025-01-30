package com.github.N1ckBaran0v.library.controller;

import com.github.N1ckBaran0v.library.component.SessionInfo;
import com.github.N1ckBaran0v.library.form.LoginForm;
import com.github.N1ckBaran0v.library.service.InvalidLoginDataException;
import com.github.N1ckBaran0v.library.service.SessionNotEndedException;
import com.github.N1ckBaran0v.library.service.UserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoginController {
    private final UserService userService;

    @Autowired
    public LoginController(@NotNull UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public LoginForm loginForm() {
        return new LoginForm();
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginForm loginForm, @NotNull SessionInfo sessionInfo) {
        var response = ResponseEntity.ok("Success");
        if (loginForm.hasErrors()) {
            response = ResponseEntity.badRequest().body("400 Bad Request");
        } else {
            try {
                userService.login(loginForm, sessionInfo);
            } catch (InvalidLoginDataException e) {
                response = ResponseEntity.badRequest().body("400 Bad Request");
            } catch (SessionNotEndedException e) {
                response = new ResponseEntity<>("409 Conflict", HttpStatus.CONFLICT);
            }
        }
        return response;
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@NotNull SessionInfo sessionInfo) {
        userService.logout(sessionInfo);
        return ResponseEntity.ok("Success");
    }
}
