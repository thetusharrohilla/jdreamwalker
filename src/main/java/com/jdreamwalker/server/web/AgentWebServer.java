package com.jdreamwalker.server.web;

import com.jdreamwalker.handler.web.iface.BaseHttpHandler;
import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;
import main.java.com.jdreamwalker.Authentication.RequestEnum;
import main.java.com.jdreamwalker.Authentication.RequestHandler;
import main.java.com.jdreamwalker.Authentication.RequestHandlerFactory;
import main.java.com.jdreamwalker.Authentication.interceptor.AuthorizationFilter;
import main.java.com.jdreamwalker.util.TransformUtil;
import org.reflections.Reflections;

import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.util.Objects;
import java.util.Set;

import static com.jdreamwalker.constant.AgentWebServerConstant.AGENT_WEB_SERVER_BASE_PATH;

public class AgentWebServer {

    private final int port;
    private final String host;

    private static HttpServer AGENT_WEB_SERVER = null;
    private static Instrumentation INSTRUMENTATION_INSTANCE = null;

    private AgentWebServer(final int port, final String host) {
        this.port = port;
        this.host = host;
    }

    public static boolean createServer(final String host, final int port, final Instrumentation instrumentation, final String agentArgs) throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        if (Objects.nonNull(AGENT_WEB_SERVER)) {
            return true;
        }
        INSTRUMENTATION_INSTANCE = instrumentation;;
        AGENT_WEB_SERVER = HttpServer.create(new InetSocketAddress(host, port), 0);
        RequestHandlerFactory RequestFactory = new RequestHandlerFactory();
        RequestHandler request = RequestFactory.createProtocol(RequestEnum.HTTP, TransformUtil.fromJson(agentArgs, Object.class));
        return tryBinding(request);
    }

    private static boolean tryBinding(RequestHandler request) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        final Reflections reflections = new Reflections();
        final Set<Class<? extends BaseHttpHandler>> httpHandlers = reflections.getSubTypesOf(BaseHttpHandler.class);
        for (final Class<? extends BaseHttpHandler> baseHttpHandlerClass : httpHandlers) {
            final BaseHttpHandler baseHttpHandler = baseHttpHandlerClass.getConstructor(Instrumentation.class)
                    .newInstance(INSTRUMENTATION_INSTANCE);
            AGENT_WEB_SERVER.createContext(AGENT_WEB_SERVER_BASE_PATH + baseHttpHandler.getBaseUrl(), baseHttpHandler).getFilters().add(new AuthorizationFilter(request));
        }

        return true;
    }

    public static boolean tryStart() {
        if (Objects.isNull(AGENT_WEB_SERVER)) {
            return false;
        }
        AGENT_WEB_SERVER.start();
        System.out.println("Started Web server");
        return true;
    }


}
