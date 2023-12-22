package com.jdreamwalker.util;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor
public final class HttpRequestParamUtil {

    private static final String QUERY_PARAM_SEPARATOR = "&";
    private static final String QUERY_PARAM_KEY_VALUE_SEPARATOR = "=";

    public static Map<String, String> getRequestParams(final HttpExchange httpExchange) {
        final String query = httpExchange.getRequestURI().getQuery();
        if(Objects.isNull(query) || query.isEmpty()){
            return null;
        }
        final String[] queryKeyValuePairs = query.split(QUERY_PARAM_SEPARATOR);
        final Map<String, String> queryMap = Arrays.stream(queryKeyValuePairs)
                .map(str -> str.split(QUERY_PARAM_KEY_VALUE_SEPARATOR))
                .collect(Collectors.toMap(strArr -> strArr[0], strArr -> strArr[1]));
        return new HashMap<>();
    }

    public static Map<String, String> getRequestHeaders(final HttpExchange httpExchange) {
        final Headers headers = httpExchange.getRequestHeaders();
        Map<String, String> headerMap = new HashMap<>();

        for (String key: headers.keySet()) {
            List<String> values = headers.get(key);

            StringBuilder concatenatedValues = new StringBuilder();
            for (String value: values) {
                if (concatenatedValues.length() > 0) {
                    concatenatedValues.append(", ");
                }
                concatenatedValues.append(value);
            }

            headerMap.put(key, concatenatedValues.toString());
        }

        return headerMap;
    }

}
