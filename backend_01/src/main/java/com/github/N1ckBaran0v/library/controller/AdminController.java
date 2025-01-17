package com.github.N1ckBaran0v.library.controller;

import com.github.N1ckBaran0v.library.data.User;
import com.github.N1ckBaran0v.library.server.EndpointContext;
import com.github.N1ckBaran0v.library.server.HttpStatus;
import com.github.N1ckBaran0v.library.server.MiniController;
import com.github.N1ckBaran0v.library.service.UnauthorizedException;
import com.github.N1ckBaran0v.library.service.UserService;
import org.jetbrains.annotations.NotNull;

public abstract class AdminController implements MiniController {
    protected final UserService userService;

    protected AdminController(@NotNull UserService userService) {
        this.userService = userService;
    }

    @Override
    public final void handle(@NotNull EndpointContext ctx) {
        try {
            var user = userService.getUser(ctx.getSessionId());
            if (User.ADMIN_ROLE.equals(user.getRole())) {
                _handle(ctx, user);
            } else {
                ctx.setResponseCode(HttpStatus.FORBIDDEN);
                ctx.setResponseBody("403 Forbidden");
            }
        } catch (UnauthorizedException e) {
            ctx.setResponseCode(HttpStatus.UNAUTHORIZED);
            ctx.setResponseBody("401 Unauthorized");
        }
    }

    protected abstract void _handle(@NotNull EndpointContext ctx, @NotNull User user);
}
