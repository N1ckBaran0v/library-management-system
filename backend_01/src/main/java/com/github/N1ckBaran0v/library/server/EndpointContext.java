package com.github.N1ckBaran0v.library.server;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class EndpointContext {
    private static final String DEFAULT_MESSAGE = "Success";

    private String responseBody = DEFAULT_MESSAGE;
    private int responseCode = HttpStatus.OK;

    private final Map<String, String> requestParams;
    private final InputStream requestBody;
    private final JsonConvertor jsonConvertor;
    private final String sessionId;

    public EndpointContext(@NotNull Map<String, String> requestParams,
                           @NotNull InputStream requestBody,
                           @NotNull JsonConvertor jsonConvertor,
                           @NotNull String sessionId) {
        this.requestParams = requestParams;
        this.requestBody = requestBody;
        this.jsonConvertor = jsonConvertor;
        this.sessionId = sessionId;
    }

    public <T> T getRequestBody(@NotNull Class<T> clazz) {
        try {
            return jsonConvertor.fromJson(requestBody, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(@NotNull Object responseBody) {
        this.responseBody = jsonConvertor.toJson(responseBody);
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public Map<String, String> getRequestParams() {
        return requestParams;
    }

    public String getSessionId() {
        return sessionId;
    }
}
