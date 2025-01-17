package com.github.N1ckBaran0v.library.controller;

import com.github.N1ckBaran0v.library.data.User;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@FunctionalInterface
public interface WorkerConsumer {
    void accept(@NotNull String username, @NotNull List<Long> books, @NotNull User user);
}
