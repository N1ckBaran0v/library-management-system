package com.github.N1ckBaran0v.library.controller;

import com.github.N1ckBaran0v.library.server.EndpointContext;
import com.github.N1ckBaran0v.library.server.HttpStatus;
import com.github.N1ckBaran0v.library.service.BookService;
import com.github.N1ckBaran0v.library.service.ForbiddenException;
import com.github.N1ckBaran0v.library.service.UserService;
import org.jetbrains.annotations.NotNull;

public class GetUserBooksController extends AuthorizedController {
    private final BookService bookService;

    public GetUserBooksController(@NotNull UserService userService, @NotNull BookService bookService) {
        super(userService);
        this.bookService = bookService;
    }

    @Override
    protected void _handle(@NotNull EndpointContext ctx, @NotNull String username) {
        try {
            var target = username;
            var params = ctx.getRequestParams();
            if (params.containsKey("username")) {
                target = params.get("username");
            }
            var user = userService.getUser(ctx.getSessionId());
            ctx.setResponseBody(bookService.findUserBooks(target, user));
        } catch (ForbiddenException e) {
            ctx.setResponseCode(HttpStatus.FORBIDDEN);
            ctx.setResponseBody("403 Forbidden");
        }
    }
}
