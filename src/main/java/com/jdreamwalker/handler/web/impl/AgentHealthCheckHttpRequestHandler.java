package com.jdreamwalker.handler.web.impl;

import com.jdreamwalker.handler.web.iface.BaseHttpHandler;
import com.sun.net.httpserver.HttpExchange;
import main.java.com.jdreamwalker.Authentication.RequestHandler;
import main.java.com.jdreamwalker.Authentication.UserRequestDto;
import main.java.com.jdreamwalker.util.TransformUtil;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.instrument.Instrumentation;

import static com.jdreamwalker.util.HttpRequestMappingUtil.generateRequestUniqueId;
import static com.jdreamwalker.util.HttpRequestParamUtil.getRequestHeaders;
import static com.jdreamwalker.util.HttpRequestParamUtil.getRequestParams;

public class AgentHealthCheckHttpRequestHandler extends BaseHttpHandler {

    private static final String HANDLER_BASE_PATH = "/health";
    private static final String HEALTH_CHECK_METHOD_AND_PATH = "GET_";


    public AgentHealthCheckHttpRequestHandler(final Instrumentation instrumentation) {
        super(instrumentation, HANDLER_BASE_PATH);
    }

    @Override
    public void handle(final HttpExchange exchange) throws IOException {

        final String requestUniqueId = generateRequestUniqueId(exchange, HANDLER_BASE_PATH);

        switch (requestUniqueId) {
            case HEALTH_CHECK_METHOD_AND_PATH:
                checkHealth(exchange);
                break;
            default:
                handleUnknownPath(exchange);

        }
    }

    private void checkHealth(final HttpExchange httpExchange) throws IOException {
        httpExchange.sendResponseHeaders(200, 0);
        final OutputStream responseBody = httpExchange.getResponseBody();
        responseBody.write("All good, All ways".getBytes());
        responseBody.close();
    }
}
