package com.debugger;

import com.sun.jdi.ThreadReference;

public class DebugOutput {
    private final ThreadReference thread;
    private final StringBuilder output;

    public DebugOutput(ThreadReference thread, StringBuilder output) {
        this.thread = thread;
        this.output = output;
    }

    public ThreadReference getThread() {
        return thread;
    }

    public StringBuilder getOutput() {
        return output;
    }
}
