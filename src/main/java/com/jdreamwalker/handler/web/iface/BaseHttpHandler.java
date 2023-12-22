package com.jdreamwalker.handler.web.iface;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import lombok.Getter;
import main.java.com.jdreamwalker.Authentication.RequestHandler;

import java.io.IOException;
import java.lang.instrument.Instrumentation;

public abstract class BaseHttpHandler implements HttpHandler {

    protected final Instrumentation instrumentation;

    @Getter
    private final String baseUrl;

    protected final RequestHandler request;

    protected BaseHttpHandler(final Instrumentation instrumentation, final String baseUrl, RequestHandler request) {
        this.instrumentation = instrumentation;
        this.baseUrl = baseUrl;
        this.request = request;

    }

    protected void handleUnknownPath(final HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(404, 0);
        exchange.getResponseBody().close();
    }

    protected void handleUnauthorizedUser(final HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(401, 0); // 401 for Unauthorized status
        exchange.getResponseBody().close();
    }
}
