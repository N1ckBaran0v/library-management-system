package com.github.N1ckBaran0v.library.server;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

public class ControllerManagerImplementation implements ControllerManager {
    private final Map<RequestMethod, Map<String, MiniController>> controllers = new ConcurrentHashMap<>();
    private final Pattern pattern = Pattern.compile("^[a-z0-9]*$");

    public ControllerManagerImplementation() {
        for (var method : RequestMethod.values()) {
            controllers.put(method, new ConcurrentHashMap<>());
        }
    }

    @Override
    public void registerController(@NotNull RequestMethod method,
                                   @NotNull String path,
                                   @NotNull MiniController controller) {
        if (path.isBlank()) {
            throw new InvalidPathException();
        }
        var parts = path.strip().toLowerCase().split("/");
        if (!"".equals(parts[0])) {
            throw new InvalidPathException();
        }
        for (var i = 1; i < parts.length; ++i) {
            if (!pattern.matcher(parts[i]).matches()) {
                throw new InvalidPathException();
            }
        }
        var methodMap = controllers.get(method);
        if (methodMap.containsKey(path)) {
            throw new ControllersConflictException();
        }
        methodMap.put(path, controller);
    }

    @Override
    public MiniController getController(@NotNull RequestMethod method, @NotNull String path) {
        var controller = controllers.get(method).get(path.strip().toLowerCase());
        if (controller == null) {
            throw new ControllerNotFoundException();
        }
        return controller;
    }
}
