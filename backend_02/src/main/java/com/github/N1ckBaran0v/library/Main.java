package com.github.N1ckBaran0v.library;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.ExecutorThreadPool;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class Main {
    private static final String CONTEXT_PATH = "/";
    private static final String DISPATCHER_PATH = "/*";
    private static final String CONFIG_PATH = "com.github.N1ckBaran0v.library.configuration";

    public static void main(String[] args) throws Exception {
        var ctx = new AnnotationConfigWebApplicationContext();
        ctx.setConfigLocation(CONFIG_PATH);
        var threadPool = new ExecutorThreadPool();
        threadPool.setVirtualThreadsExecutor(Executors.newVirtualThreadPerTaskExecutor());
        var server = new Server(threadPool);
        var connector = new ServerConnector(server);
        connector.setPort(8080);
        connector.setHost("127.0.0.1");
        server.addConnector(connector);
        var contextHandler = new ServletContextHandler();
        contextHandler.setErrorHandler(null);
        contextHandler.setContextPath(CONTEXT_PATH);
        contextHandler.addServlet(new ServletHolder(new DispatcherServlet(ctx)), DISPATCHER_PATH);
        contextHandler.addEventListener(new ContextLoaderListener(ctx));
        server.setHandler(contextHandler);
        server.start();
        Logger.getLogger("Server.class").info("Server started at " + server.getURI());
        server.join();
    }
}