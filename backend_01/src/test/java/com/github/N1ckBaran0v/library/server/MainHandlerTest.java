package com.github.N1ckBaran0v.library.server;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MainHandlerTest {
    @Mock
    private ControllerManager controllerManager;

    @InjectMocks
    private MainHandler mainHandler;

    @Mock
    private HttpExchange httpExchange;

    @Mock
    private MiniController miniController;

    @Mock
    private URI uri;

    @Mock
    private Headers headers;

    @Test
    void handleSuccess() throws IOException {
        given(httpExchange.getResponseBody()).willReturn(new ByteArrayOutputStream());
        given(httpExchange.getRequestBody()).willReturn(new ByteArrayInputStream("".getBytes()));
        given(httpExchange.getRequestURI()).willReturn(uri);
        given(uri.getPath()).willReturn("/test");
        given(httpExchange.getRequestMethod()).willReturn("GET");
        given(httpExchange.getRequestHeaders()).willReturn(headers);
        given(httpExchange.getResponseHeaders()).willReturn(headers);
        given(controllerManager.getController(RequestMethod.GET, "/test")).willReturn(miniController);
        mainHandler.handle(httpExchange);
        verify(httpExchange).setAttribute("content-type", "application/json");
        verify(httpExchange).sendResponseHeaders(HttpStatus.OK, 9);
    }

    @Test
    void internalServerError() throws IOException {
        given(httpExchange.getResponseBody()).willReturn(new ByteArrayOutputStream());
        given(httpExchange.getRequestBody()).willReturn(new ByteArrayInputStream("".getBytes()));
        given(httpExchange.getRequestURI()).willReturn(uri);
        given(uri.getPath()).willReturn("/test");
        given(httpExchange.getRequestMethod()).willReturn("GET");
        given(httpExchange.getRequestHeaders()).willReturn(headers);
        given(httpExchange.getResponseHeaders()).willReturn(headers);
        given(controllerManager.getController(RequestMethod.GET, "/test")).willReturn(miniController);
        willThrow(RuntimeException.class).given(miniController).handle(any());
        mainHandler.handle(httpExchange);
        verify(httpExchange).setAttribute("content-type", "application/json");
        verify(httpExchange).sendResponseHeaders(HttpStatus.INTERNAL_SERVER_ERROR, 25);
    }

    @Test
    void convertationException() throws IOException {
        given(httpExchange.getResponseBody()).willReturn(new ByteArrayOutputStream());
        given(httpExchange.getRequestBody()).willReturn(new ByteArrayInputStream("".getBytes()));
        given(httpExchange.getRequestURI()).willReturn(uri);
        given(uri.getPath()).willReturn("/test");
        given(httpExchange.getRequestMethod()).willReturn("GET");
        given(httpExchange.getRequestHeaders()).willReturn(headers);
        given(httpExchange.getResponseHeaders()).willReturn(headers);
        given(controllerManager.getController(RequestMethod.GET, "/test")).willReturn(miniController);
        willThrow(ConvertationException.class).given(miniController).handle(any());
        mainHandler.handle(httpExchange);
        verify(httpExchange).setAttribute("content-type", "application/json");
        verify(httpExchange).sendResponseHeaders(HttpStatus.BAD_REQUEST, 15);
    }

    @Test
    void controllerNotFound() throws IOException {
        given(httpExchange.getResponseBody()).willReturn(new ByteArrayOutputStream());
        given(httpExchange.getRequestBody()).willReturn(new ByteArrayInputStream("".getBytes()));
        given(httpExchange.getRequestURI()).willReturn(uri);
        given(uri.getPath()).willReturn("/test");
        given(httpExchange.getRequestMethod()).willReturn("GET");
        willThrow(ControllerNotFoundException.class).given(controllerManager).getController(RequestMethod.GET, "/test");
        mainHandler.handle(httpExchange);
        verify(httpExchange).setAttribute("content-type", "application/json");
        verify(httpExchange).sendResponseHeaders(HttpStatus.NOT_FOUND, 13);
    }
}