package com.github.N1ckBaran0v.library.server;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class MiniServer {
    private final HttpServer server;
    private final ControllerManager controllerManager;
    private final Logger logger = Logger.getLogger("MiniServer");

    public MiniServer(int port, @NotNull HttpHandler handler, @NotNull ControllerManager controllerManager) {
        try {
            server = HttpServer.create(new InetSocketAddress(port), 0);
            server.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
            server.createContext("/", handler);
            this.controllerManager = controllerManager;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void get(@NotNull String path, @NotNull MiniController controller) {
        controllerManager.registerController(RequestMethod.GET, path, controller);
    }

    public void post(@NotNull String path, @NotNull MiniController controller) {
        controllerManager.registerController(RequestMethod.POST, path, controller);
    }

    public void put(@NotNull String path, @NotNull MiniController controller) {
        controllerManager.registerController(RequestMethod.PUT, path, controller);
    }

    public void patch(@NotNull String path, @NotNull MiniController controller) {
        controllerManager.registerController(RequestMethod.PATCH, path, controller);
    }

    public void delete(@NotNull String path, @NotNull MiniController controller) {
        controllerManager.registerController(RequestMethod.DELETE, path, controller);
    }

    public void head(@NotNull String path, @NotNull MiniController controller) {
        controllerManager.registerController(RequestMethod.HEAD, path, controller);
    }

    public void options(@NotNull String path, @NotNull MiniController controller) {
        controllerManager.registerController(RequestMethod.OPTIONS, path, controller);
    }

    public void start() {
        server.start();
        logger.info("Server started at: " + server.getAddress().getPort());
    }
}
