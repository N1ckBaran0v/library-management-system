package com.github.N1ckBaran0v.library.context;

import com.github.N1ckBaran0v.library.server.MiniServer;
import org.jetbrains.annotations.NotNull;

public class ControllerContext {
    public void setEndpoints(@NotNull MiniServer server) {
        server.get("/thread", ctx -> ctx.setResponseBody(Thread.currentThread().toString()));
    }
}
