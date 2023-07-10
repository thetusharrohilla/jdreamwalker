package com.jdreamwalker.handler.web.iface;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import lombok.Getter;

import java.io.IOException;
import java.lang.instrument.Instrumentation;

public abstract class BaseHttpHandler implements HttpHandler {

    protected final Instrumentation instrumentation;

    @Getter
    private final String baseUrl;

    protected BaseHttpHandler(final Instrumentation instrumentation, final String baseUrl) {
        this.instrumentation = instrumentation;
        this.baseUrl = baseUrl;
    }

    protected void handleUnknownPath(final HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(404, 0);
        exchange.getResponseBody().close();
    }

}
