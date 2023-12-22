package com.jdreamwalker;


import java.io.IOException;
import java.lang.instrument.Instrumentation;

public class AgentMain {

    public static void premain(final String agentArgs, final Instrumentation instrumentation) throws InterruptedException, IOException {
        com.jdreamwalker.JDreamWalkerAgent.initAgent(instrumentation,agentArgs);
    }

    public static void agentmain(final String agentArgs, final Instrumentation instrumentation) {
        throw new UnsupportedOperationException("JDreamWalker doesn't support dynamic attachment");
    }
}