package com.github.N1ckBaran0v.library.controller;

import com.github.N1ckBaran0v.library.data.Book;
import com.github.N1ckBaran0v.library.data.User;
import com.github.N1ckBaran0v.library.service.BookNotFoundException;
import com.github.N1ckBaran0v.library.server.EndpointContext;
import com.github.N1ckBaran0v.library.server.HttpStatus;
import com.github.N1ckBaran0v.library.service.BookService;
import com.github.N1ckBaran0v.library.service.UserService;
import org.jetbrains.annotations.NotNull;

public class PostBooksUpdateController extends AdminController {
    private final BookService bookService;

    public PostBooksUpdateController(@NotNull UserService userService, @NotNull BookService bookService) {
        super(userService);
        this.bookService = bookService;
    }

    @Override
    protected void _handle(@NotNull EndpointContext ctx, @NotNull User user) {
        try {
            var book = ctx.getRequestBody(Book.class);
            if (book.hasErrors() || book.getId() == null) {
                ctx.setResponseCode(HttpStatus.BAD_REQUEST);
                ctx.setResponseBody("400 Bad Request");
            } else {
                bookService.updateBook(book, user);
                ctx.setResponseCode(HttpStatus.ACCEPTED);
                ctx.setResponseBody("202 Accepted");
            }
        } catch (BookNotFoundException e) {
            ctx.setResponseCode(HttpStatus.BAD_REQUEST);
            ctx.setResponseBody("400 Bad Request");
        }
    }
}
