package com.jdreamwalker.util;

import com.sun.net.httpserver.HttpExchange;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static com.jdreamwalker.constant.AgentWebServerConstant.AGENT_WEB_SERVER_BASE_PATH;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HttpRequestMappingUtil {

    private static final String REQUEST_ID_TEMPLATE = "%s_%s";

    public static String generateRequestUniqueId(final HttpExchange exchange, String handlerBasePath) {
        final String requestUri = exchange.getRequestURI().getPath();
        final String handlerLocalPath = requestUri.replaceAll(AGENT_WEB_SERVER_BASE_PATH, "")
                .replaceAll(handlerBasePath, "");
        final String requestMethod = exchange.getRequestMethod();
        return String.format(REQUEST_ID_TEMPLATE, requestMethod, handlerLocalPath);
    }

}
