package com.github.N1ckBaran0v.library.controller;

import com.github.N1ckBaran0v.library.data.User;
import com.github.N1ckBaran0v.library.server.EndpointContext;
import com.github.N1ckBaran0v.library.server.HttpStatus;
import com.github.N1ckBaran0v.library.service.UserNotFoundException;
import com.github.N1ckBaran0v.library.service.UserService;
import org.jetbrains.annotations.NotNull;

public class PostUserDeleteController extends AdminController {
    public PostUserDeleteController(@NotNull UserService userService) {
        super(userService);
    }

    @Override
    protected void _handle(@NotNull EndpointContext ctx, @NotNull User user) {
        try {
            var target = ctx.getRequestParams().get("username");
            if (target == null) {
                throw new IllegalArgumentException();
            }
            userService.deleteUser(ctx.getSessionId(), target);
            ctx.setResponseCode(HttpStatus.NO_CONTENT);
            ctx.setResponseBody("204 No Content");
        } catch (IllegalArgumentException | UserNotFoundException e) {
            ctx.setResponseCode(HttpStatus.BAD_REQUEST);
            ctx.setResponseBody("400 Bad Request");
        }
    }
}
