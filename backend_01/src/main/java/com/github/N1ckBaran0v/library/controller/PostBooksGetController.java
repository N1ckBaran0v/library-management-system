package com.github.N1ckBaran0v.library.controller;

import com.github.N1ckBaran0v.library.service.BookService;
import com.github.N1ckBaran0v.library.service.UserService;
import org.jetbrains.annotations.NotNull;

public class PostBooksGetController extends WorkerController {
    public PostBooksGetController(@NotNull UserService userService, @NotNull BookService bookService) {
        super(userService, bookService::getBooks);
    }
}
