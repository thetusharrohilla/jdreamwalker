package com.jdreamwalker;

import com.jdreamwalker.server.web.AgentWebServer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.InvocationTargetException;

@Slf4j
public class JDreamWalkerAgent {

    private static final String DREAM_WALKER_HOST = "0.0.0.0";
    private static final int DREAM_WALKER_PORT = 8123;

    public static void initAgent(final Instrumentation instrumentation, final String agentArgs) {
        try {
            AgentWebServer.createServer(DREAM_WALKER_HOST, DREAM_WALKER_PORT, instrumentation, agentArgs);
            AgentWebServer.tryStart();
        } catch (final Exception e) {
            log.error("Unable to initialise JDreamWalker. Error Message : {}", e.getMessage(), e);
        }
    }

}
