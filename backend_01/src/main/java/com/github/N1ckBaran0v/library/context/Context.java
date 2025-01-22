package com.github.N1ckBaran0v.library.context;

import com.github.N1ckBaran0v.library.server.MiniServer;

public class Context {
    private final MiniServer server;

    public Context(int port) {
        var serverContext = new ServerContext(port);
        server = serverContext.getServer();
        var databaseContext = new DatabaseContext();
        var serviceContext = new ServiceContext(databaseContext);
        var controllerContext = new ControllerContext(serviceContext);
        controllerContext.setEndpoints(server);
    }

    public MiniServer getServer() {
        return server;
    }
}
