package com.jdreamwalker.server.web;

import com.jdreamwalker.handler.web.iface.BaseHttpHandler;
import com.sun.net.httpserver.HttpServer;
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

    public static boolean createServer(final String host, final int port, final Instrumentation instrumentation) throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        if (Objects.nonNull(AGENT_WEB_SERVER)) {
            return true;
        }
        INSTRUMENTATION_INSTANCE = instrumentation;
        AGENT_WEB_SERVER = HttpServer.create(new InetSocketAddress(host, port), 0);
        return tryBinding();
    }

    private static boolean tryBinding() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        final Reflections reflections = new Reflections();
        final Set<Class<? extends BaseHttpHandler>> httpHandlers = reflections.getSubTypesOf(BaseHttpHandler.class);
        for (final Class<? extends BaseHttpHandler> baseHttpHandlerClass : httpHandlers) {
            final BaseHttpHandler baseHttpHandler = baseHttpHandlerClass.getConstructor(Instrumentation.class)
                    .newInstance(INSTRUMENTATION_INSTANCE);
            AGENT_WEB_SERVER.createContext(AGENT_WEB_SERVER_BASE_PATH + baseHttpHandler.getBaseUrl(), baseHttpHandler);
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
