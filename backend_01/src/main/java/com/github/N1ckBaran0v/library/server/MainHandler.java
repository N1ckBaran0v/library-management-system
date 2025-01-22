package com.github.N1ckBaran0v.library.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.TreeMap;
import java.util.UUID;

public class MainHandler implements HttpHandler {
    private final ControllerManager controllerManager;
    private final JsonConvertor jsonConvertor;

    public MainHandler(@NotNull ControllerManager controllerManager, @NotNull JsonConvertor jsonConvertor) {
        this.controllerManager = controllerManager;
        this.jsonConvertor = jsonConvertor;
    }

    @Override
    public void handle(@NotNull HttpExchange httpExchange) throws IOException {
        var output = httpExchange.getResponseBody();
        var input = httpExchange.getRequestBody();
        var responseBody = "";
        var responseCode = HttpStatus.OK;
        try {
            var path = httpExchange.getRequestURI().getPath();
            var requestMethod = RequestMethod.valueOf(httpExchange.getRequestMethod());
            var controller = controllerManager.getController(requestMethod, path);
            var ctx = getEndpointContext(httpExchange, input, findSessionId(httpExchange));
            controller.handle(ctx);
            responseBody = ctx.getResponseBody();
            responseCode = ctx.getResponseCode();
        } catch (ControllerNotFoundException e) {
            responseCode = HttpStatus.NOT_FOUND;
            responseBody = "404 Not Found";
        } catch (InvalidRequestParamsException | ConvertationException e) {
            responseCode = HttpStatus.BAD_REQUEST;
            responseBody = "400 Bad Request";
        } catch (Exception e) {
            responseCode = HttpStatus.INTERNAL_SERVER_ERROR;
            responseBody = "500 Internal Server Error";
        } finally {
            httpExchange.setAttribute("content-type", "application/json");
            httpExchange.sendResponseHeaders(responseCode, responseBody.length());
            output.write(responseBody.getBytes());
            input.close();
            output.close();
            httpExchange.close();
        }
    }

    private EndpointContext getEndpointContext(@NotNull HttpExchange httpExchange,
                                               @NotNull InputStream input,
                                               @NotNull String sessionId) {
        var params = new TreeMap<String, String>();
        var query = httpExchange.getRequestURI().getQuery();
        if (query != null) {
            var arr = query.split("&");
            for (var rawPair : arr) {
                var pair = rawPair.split("=");
                if (pair.length != 2) {
                    throw new InvalidRequestParamsException();
                }
                if (params.containsKey(pair[0])) {
                    throw new InvalidRequestParamsException();
                }
                params.put(pair[0], pair[1]);
            }
        }
        return new EndpointContext(params, input, jsonConvertor, sessionId);
    }

    private String findSessionId(@NotNull HttpExchange httpExchange) {
        String answer = null;
        var cookies = httpExchange.getRequestHeaders().getOrDefault("Cookie", null);
        if (cookies != null) {
            for (var cookie : cookies) {
                if (cookie.startsWith("sessionId=")) {
                    answer = cookie;
                    break;
                }
            }
        }
        if (answer == null) {
            answer = "sessionId=" + UUID.randomUUID();
            httpExchange.getResponseHeaders().add("Set-Cookie", answer);
        }
        return answer;
    }
}
