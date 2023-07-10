package com.jdreamwalker;


import java.io.IOException;
import java.lang.instrument.Instrumentation;

public class AgentMain {

    public static void premain(final String agentArgs, final Instrumentation instrumentation) throws InterruptedException, IOException {
        JDreamWalkerAgent.initAgent(instrumentation);
    }

    public static void agentmain(final String agentArgs, final Instrumentation instrumentation) {
        throw new UnsupportedOperationException("JDreamWalker doesn't support dynamic attachment");
    }
}