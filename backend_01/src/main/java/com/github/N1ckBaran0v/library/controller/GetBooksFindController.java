package com.github.N1ckBaran0v.library.controller;

import com.github.N1ckBaran0v.library.form.SearchForm;
import com.github.N1ckBaran0v.library.server.EndpointContext;
import com.github.N1ckBaran0v.library.server.HttpStatus;
import com.github.N1ckBaran0v.library.service.BookService;
import com.github.N1ckBaran0v.library.service.ForbiddenException;
import com.github.N1ckBaran0v.library.service.UserService;
import org.jetbrains.annotations.NotNull;

public class GetBooksFindController extends AuthorizedController {
    private final BookService bookService;

    public GetBooksFindController(@NotNull UserService userService, @NotNull BookService bookService) {
        super(userService);
        this.bookService = bookService;
    }

    @Override
    protected void _handle(@NotNull EndpointContext ctx, @NotNull String username) {
        try {
            var form = ctx.getRequestBody(SearchForm.class);
            var user = userService.getUser(ctx.getSessionId());
            ctx.setResponseBody(bookService.findBooks(form, user));
        } catch (ForbiddenException e) {
            ctx.setResponseCode(HttpStatus.FORBIDDEN);
            ctx.setResponseBody("403 Forbidden");
        }
    }
}
