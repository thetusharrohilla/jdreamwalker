package main.java.com.jdreamwalker.Authentication.interceptor;

import com.sun.net.httpserver.HttpExchange;
import main.java.com.jdreamwalker.Authentication.RequestHandler;
import main.java.com.jdreamwalker.Authentication.UserRequestDto;
import main.java.com.jdreamwalker.util.TransformUtil;

import static com.jdreamwalker.util.HttpRequestParamUtil.getRequestHeaders;
import static com.jdreamwalker.util.HttpRequestParamUtil.getRequestParams;

public class AuthorizationInterceptor implements RequestInterceptor {
    private final RequestHandler request;

    public AuthorizationInterceptor(RequestHandler request) {
        this.request = request;
    }

    @Override
    public Boolean intercept(HttpExchange exchange) {
        UserRequestDto userRequestDto = new UserRequestDto(
                TransformUtil.convertValue(exchange.getRequestBody(), Object.class),
                getRequestHeaders(exchange),
                getRequestParams(exchange)
        );

        return request.authorize(userRequestDto);
    }
}