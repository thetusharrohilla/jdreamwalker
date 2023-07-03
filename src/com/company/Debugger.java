package com.company;

public interface Debugger {
    void connect(String host, int port) throws Exception;
    void setBreakpoint(String className, int lineNumber) throws Exception;
    void disconnect() throws Exception;
}
