package main.java.com.jdreamwalker.Authentication.interceptor;

import com.sun.net.httpserver.HttpExchange;
public interface RequestInterceptor {
    Boolean intercept(HttpExchange exchange);
}