package com.github.N1ckBaran0v.library.controller;

import com.github.N1ckBaran0v.library.server.EndpointContext;
import com.github.N1ckBaran0v.library.server.HttpStatus;
import com.github.N1ckBaran0v.library.service.BookService;
import com.github.N1ckBaran0v.library.service.ForbiddenException;
import com.github.N1ckBaran0v.library.service.UserService;
import org.jetbrains.annotations.NotNull;

public class GetUserInfoController extends AuthorizedController {
    public GetUserInfoController(@NotNull UserService userService) {
        super(userService);
    }

    @Override
    protected void _handle(@NotNull EndpointContext ctx, @NotNull String username) {
        try {
            var target = username;
            var params = ctx.getRequestParams();
            if (params.containsKey("username")) {
                target = params.get("username");
            }
            ctx.setResponseBody(userService.getUser(ctx.getSessionId(), target));
        } catch (ForbiddenException e) {
            ctx.setResponseCode(HttpStatus.FORBIDDEN);
            ctx.setResponseBody("403 Forbidden");
        }
    }
}
