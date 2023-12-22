package main.java.com.jdreamwalker.Authentication.interceptor;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;
import main.java.com.jdreamwalker.Authentication.RequestHandler;
import main.java.com.jdreamwalker.Authentication.UserRequestDto;
import main.java.com.jdreamwalker.util.TransformUtil;

import java.io.IOException;

import static com.jdreamwalker.util.HttpRequestParamUtil.getRequestHeaders;
import static com.jdreamwalker.util.HttpRequestParamUtil.getRequestParams;

public class AuthorizationFilter extends Filter {

    private final RequestHandler request;

    public AuthorizationFilter(RequestHandler request){
        this.request = request;
    }

    @Override
    public void doFilter(HttpExchange exchange, Chain chain) throws IOException {

        System.out.println("hii i am inside do filter every time");
        boolean authorized = checkAuthorization(exchange);

        if (authorized) {
            chain.doFilter(exchange);
        } else {
            // Handle unauthorized access
            handleUnauthorizedUser(exchange);
        }
    }

    private boolean checkAuthorization(HttpExchange exchange) {
        UserRequestDto userRequestDto = new UserRequestDto(
                TransformUtil.convertValue(exchange.getRequestBody(), Object.class),
                getRequestHeaders(exchange),
                getRequestParams(exchange)
        );

        return request.authorize(userRequestDto);
    }

    private void handleUnauthorizedUser(HttpExchange exchange) {
        try {
            exchange.sendResponseHeaders(401, 0);
            exchange.getResponseBody().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String description() {
        return "Authorization Filter";
    }


}