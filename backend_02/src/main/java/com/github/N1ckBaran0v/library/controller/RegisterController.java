package com.github.N1ckBaran0v.library.controller;

import com.github.N1ckBaran0v.library.component.SessionInfo;
import com.github.N1ckBaran0v.library.form.RegisterForm;
import com.github.N1ckBaran0v.library.service.SessionNotEndedException;
import com.github.N1ckBaran0v.library.service.UserAlreadyExistsException;
import com.github.N1ckBaran0v.library.service.UserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/register")
public class RegisterController {
    private final UserService userService;

    @Autowired
    public RegisterController(@NotNull UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public RegisterForm register() {
        return new RegisterForm();
    }

    @PostMapping
    public ResponseEntity<?> register(@RequestBody RegisterForm form, @NotNull SessionInfo sessionInfo) {
        var response = ResponseEntity.ok().build();
        if (form.hasErrors()) {
            response = ResponseEntity.badRequest().body("Invalid register data");
        } else {
            try {
                userService.register(sessionInfo, form);
            } catch (UserAlreadyExistsException e) {
                response = new ResponseEntity<>("User already exists", HttpStatus.CONFLICT);
            } catch (SessionNotEndedException e) {
                response = new ResponseEntity<>("Session not ended", HttpStatus.CONFLICT);
            }
        }
        return response;
    }
}
