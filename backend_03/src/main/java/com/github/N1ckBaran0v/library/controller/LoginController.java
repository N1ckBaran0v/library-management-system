package com.github.N1ckBaran0v.library.controller;

import com.github.N1ckBaran0v.library.component.SessionInfo;
import com.github.N1ckBaran0v.library.form.LoginForm;
import com.github.N1ckBaran0v.library.service.InvalidLoginDataException;
import com.github.N1ckBaran0v.library.service.SessionNotEndedException;
import com.github.N1ckBaran0v.library.service.UserService;
import jakarta.validation.Valid;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
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
    public ResponseEntity<?> login(@RequestBody @Valid LoginForm loginForm,
                                        @NotNull SessionInfo sessionInfo,
                                        @NotNull Errors errors) {
        var response = ResponseEntity.ok().build();
        if (errors.hasErrors()) {
            response = ResponseEntity.badRequest().body("Invalid login data");
        } else {
            try {
                userService.login(sessionInfo, loginForm);
            } catch (InvalidLoginDataException e) {
                response = ResponseEntity.badRequest().body("Invalid login data");
            } catch (SessionNotEndedException e) {
                response = new ResponseEntity<>("Session not ended", HttpStatus.CONFLICT);
            }
        }
        return response;
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@NotNull SessionInfo sessionInfo) {
        userService.logout(sessionInfo);
        return ResponseEntity.ok().build();
    }
}
