package com.github.N1ckBaran0v.library.controller;

import com.github.N1ckBaran0v.library.service.BookService;
import com.github.N1ckBaran0v.library.service.UserService;
import org.jetbrains.annotations.NotNull;

public class PostBooksReturnController extends WorkerController {
    public PostBooksReturnController(@NotNull UserService userService, @NotNull BookService bookService) {
        super(userService, bookService::returnBooks);
    }
}
