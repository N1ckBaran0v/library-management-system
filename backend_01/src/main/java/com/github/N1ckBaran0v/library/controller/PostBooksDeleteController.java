package com.github.N1ckBaran0v.library.controller;

import com.github.N1ckBaran0v.library.data.User;
import com.github.N1ckBaran0v.library.server.EndpointContext;
import com.github.N1ckBaran0v.library.server.HttpStatus;
import com.github.N1ckBaran0v.library.service.BookNotFoundException;
import com.github.N1ckBaran0v.library.service.BookService;
import com.github.N1ckBaran0v.library.service.UserService;
import org.jetbrains.annotations.NotNull;

public class PostBooksDeleteController extends AdminController {
    private final BookService bookService;

    public PostBooksDeleteController(@NotNull UserService userService, @NotNull BookService bookService) {
        super(userService);
        this.bookService = bookService;
    }

    @Override
    protected void _handle(@NotNull EndpointContext ctx, @NotNull User user) {
        try {
            bookService.deleteBook(ctx.getRequestBody(Long.class), user);
            ctx.setResponseCode(HttpStatus.NO_CONTENT);
            ctx.setResponseBody("204 No Content");
        } catch (BookNotFoundException e) {
            ctx.setResponseCode(HttpStatus.BAD_REQUEST);
            ctx.setResponseBody("400 Bad Request");
        }
    }
}
