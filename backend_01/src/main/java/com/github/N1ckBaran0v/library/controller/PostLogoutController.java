package com.github.N1ckBaran0v.library.controller;

import com.github.N1ckBaran0v.library.server.EndpointContext;
import com.github.N1ckBaran0v.library.service.UserService;
import org.jetbrains.annotations.NotNull;

public class PostLogoutController extends AuthorizedController {
    public PostLogoutController(@NotNull UserService userService) {
        super(userService);
    }

    @Override
    protected void _handle(@NotNull EndpointContext ctx, @NotNull String username) {
        userService.logout(username);
    }
}
