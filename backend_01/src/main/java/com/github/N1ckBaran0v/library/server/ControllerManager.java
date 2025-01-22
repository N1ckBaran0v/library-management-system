package com.github.N1ckBaran0v.library.server;

import org.jetbrains.annotations.NotNull;

public interface ControllerManager {
    void registerController(@NotNull RequestMethod method, @NotNull String path, @NotNull MiniController controller);
    MiniController getController(@NotNull RequestMethod method, @NotNull String path);
}
