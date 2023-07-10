package com.jdreamwalker.handler.web.impl;


import com.jdreamwalker.handler.web.iface.BaseHttpHandler;
import com.jdreamwalker.service.VariableLogAdditionService;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.util.Map;

import static com.jdreamwalker.util.HttpRequestMappingUtil.generateRequestUniqueId;
import static com.jdreamwalker.util.HttpRequestParamUtil.getRequestParams;

public class AgentLogManipulationHttpRequestHandler extends BaseHttpHandler {

    private static final String HANDLER_BASE_PATH = "/logging";
    private static final String PUT_LOG_POINT_URL = "PUT_/log-point";

    private static final String QUERY_PARAM_CLASS_NAME = "class_name";
    private static final String QUERY_PARAM_METHOD_NAME = "method_name";
    private static final String QUERY_PARAM_LINE_NUMBER = "line_number";
    private static final String QUERY_PARAM_VARIABLE_NAME = "variable_to_log";

    private final VariableLogAdditionService variableLogAdditionService;
    public AgentLogManipulationHttpRequestHandler(final Instrumentation instrumentation) {
        super(instrumentation, HANDLER_BASE_PATH);
        this.variableLogAdditionService = new VariableLogAdditionService(instrumentation);
    }

    @Override
    public void handle(final HttpExchange exchange) throws IOException {
        final String requestUniqueId = generateRequestUniqueId(exchange, HANDLER_BASE_PATH);
        switch (requestUniqueId) {
            case PUT_LOG_POINT_URL:
                putLogPoint(exchange);
                break;
            default:
                handleUnknownPath(exchange);
        }
    }

    private void putLogPoint(final HttpExchange httpExchange) throws IOException {
        final Map<String, String> queryParams = getRequestParams(httpExchange);
        final String className = queryParams.get(QUERY_PARAM_CLASS_NAME);
        final String methodName = queryParams.get(QUERY_PARAM_METHOD_NAME);
        final String variableToLog = queryParams.get(QUERY_PARAM_VARIABLE_NAME);
        final int lineNumber = Integer.parseInt(queryParams.get(QUERY_PARAM_LINE_NUMBER));
        this.variableLogAdditionService.addVariableLogLine(className, methodName,lineNumber, variableToLog);
        httpExchange.sendResponseHeaders(200, 0);
        httpExchange.getResponseBody().write("Success".getBytes());
        httpExchange.getResponseBody().close();
    }
}
