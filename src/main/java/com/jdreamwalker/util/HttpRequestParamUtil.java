package com.jdreamwalker.util;

import com.sun.net.httpserver.HttpExchange;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@NoArgsConstructor
public final class HttpRequestParamUtil {

    private static final String QUERY_PARAM_SEPARATOR = "&";
    private static final String QUERY_PARAM_KEY_VALUE_SEPARATOR = "=";

    public static Map<String, String> getRequestParams(final HttpExchange httpExchange) {
        final String query = httpExchange.getRequestURI().getQuery();
        final String[] queryKeyValuePairs = query.split(QUERY_PARAM_SEPARATOR);
        final Map<String, String> queryMap = Arrays.stream(queryKeyValuePairs)
                .map(str -> str.split(QUERY_PARAM_KEY_VALUE_SEPARATOR))
                .collect(Collectors.toMap(strArr -> strArr[0], strArr -> strArr[1]));
        return queryMap;
    }

}
