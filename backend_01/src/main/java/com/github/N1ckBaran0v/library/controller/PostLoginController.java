package com.github.N1ckBaran0v.library.controller;

import com.github.N1ckBaran0v.library.form.LoginForm;
import com.github.N1ckBaran0v.library.server.EndpointContext;
import com.github.N1ckBaran0v.library.server.HttpStatus;
import com.github.N1ckBaran0v.library.server.MiniController;
import com.github.N1ckBaran0v.library.service.InvalidLoginDataException;
import com.github.N1ckBaran0v.library.service.SessionNotEndedException;
import com.github.N1ckBaran0v.library.service.UserService;
import org.jetbrains.annotations.NotNull;

public class PostLoginController implements MiniController {
    private final UserService userService;

    public PostLoginController(@NotNull UserService userService) {
        this.userService = userService;
    }

    @Override
    public void handle(@NotNull EndpointContext ctx) {
        try {
            var form = ctx.getRequestBody(LoginForm.class);
            if (form.hasErrors()) {
                ctx.setResponseCode(HttpStatus.BAD_REQUEST);
                ctx.setResponseBody("400 Bad Request");
            } else {
                userService.login(form, ctx.getSessionId());
            }
        } catch (InvalidLoginDataException e) {
            ctx.setResponseCode(HttpStatus.BAD_REQUEST);
            ctx.setResponseBody("400 Bad Request");
        } catch (SessionNotEndedException e) {
            ctx.setResponseCode(HttpStatus.CONFLICT);
            ctx.setResponseBody("409 Conflict");
        }
    }
}
