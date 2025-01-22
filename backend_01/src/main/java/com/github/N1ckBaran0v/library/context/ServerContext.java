package com.github.N1ckBaran0v.library.context;

import com.github.N1ckBaran0v.library.server.*;
import com.sun.net.httpserver.HttpHandler;

public class ServerContext {
    private MiniServer server;
    private MainHandler handler;
    private JsonConvertor jsonConvertor;
    private ControllerManager controllerManager;
    private final int port;

    public ServerContext(int port) {
        this.port = port;
    }

    public MiniServer getServer() {
        if (server == null) {
            server = new MiniServer(port, getHandler(), getControllerManager());
            addControllers();
        }
        return server;
    }

    private HttpHandler getHandler() {
        if (handler == null) {
            handler = new MainHandler(getControllerManager(), getJsonConvertor());
        }
        return handler;
    }

    private ControllerManager getControllerManager() {
        if (controllerManager == null) {
            controllerManager = new ControllerManagerImplementation();
        }
        return controllerManager;
    }

    private JsonConvertor getJsonConvertor() {
        if (jsonConvertor == null) {
            jsonConvertor = new GsonConvertor();
        }
        return jsonConvertor;
    }

    private void addControllers() {
        server.get("/thread", ctx -> ctx.setResponseBody(Thread.currentThread().toString()));
    }
}
