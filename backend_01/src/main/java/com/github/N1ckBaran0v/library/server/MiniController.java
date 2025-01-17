package com.github.N1ckBaran0v.library.server;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface MiniController {
    void handle(@NotNull EndpointContext ctx);
}
