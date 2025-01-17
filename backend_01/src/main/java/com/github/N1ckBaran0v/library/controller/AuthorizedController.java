package com.github.N1ckBaran0v.library.controller;

import com.github.N1ckBaran0v.library.server.EndpointContext;
import com.github.N1ckBaran0v.library.server.HttpStatus;
import com.github.N1ckBaran0v.library.server.MiniController;
import com.github.N1ckBaran0v.library.service.UnauthorizedException;
import com.github.N1ckBaran0v.library.service.UserService;
import org.jetbrains.annotations.NotNull;

public abstract class AuthorizedController implements MiniController {
    protected final UserService userService;

    protected AuthorizedController(@NotNull UserService userService) {
        this.userService = userService;
    }

    @Override
    public void handle(@NotNull EndpointContext ctx) {
        try {
            _handle(ctx, userService.getUsername(ctx.getSessionId()));
        } catch (UnauthorizedException e) {
            ctx.setResponseCode(HttpStatus.UNAUTHORIZED);
            ctx.setResponseBody("401 Unauthorized");
        }
    }

    abstract protected void _handle(@NotNull EndpointContext ctx, @NotNull String username);
}
