package com.company;

import com.sun.jdi.VirtualMachine;

public class Main {
    private VirtualMachine vm;

    public static void main(String[] args) {
        Debugger debugger = new CustomDebugger();

        try {
            debugger.connect("localhost", 9314);
            debugger.setBreakpoint("com.example.MyClass", 42);
            // Perform other operations with the debugger

            debugger.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

