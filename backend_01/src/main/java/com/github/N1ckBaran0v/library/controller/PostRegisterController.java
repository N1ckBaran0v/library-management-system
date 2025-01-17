package com.github.N1ckBaran0v.library.controller;

import com.github.N1ckBaran0v.library.form.RegisterForm;
import com.github.N1ckBaran0v.library.server.EndpointContext;
import com.github.N1ckBaran0v.library.server.HttpStatus;
import com.github.N1ckBaran0v.library.server.MiniController;
import com.github.N1ckBaran0v.library.service.ForbiddenException;
import com.github.N1ckBaran0v.library.service.SessionNotEndedException;
import com.github.N1ckBaran0v.library.service.UserAlreadyExistsException;
import com.github.N1ckBaran0v.library.service.UserService;
import org.jetbrains.annotations.NotNull;

public class PostRegisterController implements MiniController {
    private final UserService userService;

    public PostRegisterController(@NotNull UserService userService) {
        this.userService = userService;
    }

    @Override
    public void handle(@NotNull EndpointContext ctx) {
        try {
            var form = ctx.getRequestBody(RegisterForm.class);
            if (form.hasErrors()) {
                ctx.setResponseCode(HttpStatus.BAD_REQUEST);
                ctx.setResponseBody("400 Bad Request");
            } else {
                userService.register(form, ctx.getSessionId());
                ctx.setResponseCode(HttpStatus.CREATED);
            }
        } catch (UserAlreadyExistsException | SessionNotEndedException exception) {
            ctx.setResponseCode(HttpStatus.CONFLICT);
            ctx.setResponseBody("409 Conflict");
        } catch (ForbiddenException exception) {
            ctx.setResponseCode(HttpStatus.FORBIDDEN);
            ctx.setResponseBody("403 Forbidden");
        }
    }
}
