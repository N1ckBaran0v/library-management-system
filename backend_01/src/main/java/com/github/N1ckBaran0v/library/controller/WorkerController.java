package com.github.N1ckBaran0v.library.controller;

import com.github.N1ckBaran0v.library.server.EndpointContext;
import com.github.N1ckBaran0v.library.server.HttpStatus;
import com.github.N1ckBaran0v.library.service.ForbiddenException;
import com.github.N1ckBaran0v.library.service.UserService;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;

public abstract class WorkerController extends AuthorizedController {
    private final WorkerConsumer consumer;

    protected WorkerController(@NotNull UserService userService, @NotNull WorkerConsumer consumer) {
        super(userService);
        this.consumer = consumer;
    }

    @Override
    protected final void _handle(@NotNull EndpointContext ctx, @NotNull String username) {
        try {
            var target = ctx.getRequestParams().getOrDefault("username", null);
            if (target == null) {
                throw new IllegalArgumentException();
            }
            var books = Arrays.stream(ctx.getRequestBody(Long[].class)).filter(Objects::nonNull).toList();
            if (books.isEmpty()) {
                throw new IllegalArgumentException();
            }
            consumer.accept(target, books, userService.getUser(ctx.getSessionId()));
        } catch (ForbiddenException e) {
            ctx.setResponseCode(HttpStatus.FORBIDDEN);
            ctx.setResponseBody("403 Forbidden");
        } catch (IllegalArgumentException e) {
            ctx.setResponseCode(HttpStatus.BAD_REQUEST);
            ctx.setResponseBody("400 Bad Request");
        }
    }
}
