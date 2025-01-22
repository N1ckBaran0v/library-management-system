package com.github.N1ckBaran0v.library.context;

import com.github.N1ckBaran0v.library.controller.*;
import com.github.N1ckBaran0v.library.server.MiniServer;
import org.jetbrains.annotations.NotNull;

public class ControllerContext {
    private final ServiceContext ctx;

    public ControllerContext(@NotNull ServiceContext ctx) {
        this.ctx = ctx;
    }

    public void setEndpoints(@NotNull MiniServer server) {
        server.get("/books/find", new GetBooksFindController(ctx.getUserService(), ctx.getBookService()));
        server.get("/login", new GetLoginController());
        server.get("/register", new GetRegisterController());
        server.get("/user/books", new GetUserBooksController(ctx.getUserService(), ctx.getBookService()));
        server.get("/user/history", new GetUserHistoryController(ctx.getUserService(), ctx.getBookService()));
        server.get("/user/info", new GetUserInfoController(ctx.getUserService()));
        server.post("/books/create", new PostBooksCreateController(ctx.getUserService(), ctx.getBookService()));
        server.post("/books/delete", new PostBooksDeleteController(ctx.getUserService(), ctx.getBookService()));
        server.post("/books/update", new PostBooksUpdateController(ctx.getUserService(), ctx.getBookService()));
        server.post("/books/get", new PostBooksGetController(ctx.getUserService(), ctx.getBookService()));
        server.post("/books/return", new PostBooksReturnController(ctx.getUserService(), ctx.getBookService()));
        server.post("/login", new PostLoginController(ctx.getUserService()));
        server.post("/register", new PostRegisterController(ctx.getUserService()));
        server.post("/logout", new PostLogoutController(ctx.getUserService()));
        server.post("/user/delete", new PostUserDeleteController(ctx.getUserService()));
    }
}
