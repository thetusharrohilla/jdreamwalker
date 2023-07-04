package com.debugger;

import com.sun.jdi.AbsentInformationException;
import com.sun.jdi.Bootstrap;
import com.sun.jdi.IncompatibleThreadStateException;
import com.sun.jdi.InvalidStackFrameException;
import com.sun.jdi.LocalVariable;
import com.sun.jdi.Location;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.StackFrame;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.VMDisconnectedException;
import com.sun.jdi.Value;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.VirtualMachineManager;
import com.sun.jdi.connect.AttachingConnector;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.event.BreakpointEvent;
import com.sun.jdi.event.Event;
import com.sun.jdi.event.EventQueue;
import com.sun.jdi.event.EventSet;
import com.sun.jdi.request.BreakpointRequest;
import com.sun.jdi.request.EventRequestManager;

import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

public class CustomDebugger implements Debugger {

    private static final Logger logger = Logger.getLogger(CustomDebugger.class.getName());

    private final ExecutorService executorService;
    private final BlockingQueue<DebugOutput> outputQueue;
    private VirtualMachine vm;

    public CustomDebugger() {
        executorService = Executors.newSingleThreadExecutor();
        outputQueue = new LinkedBlockingQueue<>();
    }

    @Override
    public void connect(String host, int port) throws Exception {
        VirtualMachineManager vmManager = Bootstrap.virtualMachineManager();
        AttachingConnector attachingConnector = null;
        for (Connector connector : vmManager.attachingConnectors()) {
            if ("com.sun.jdi.SocketAttach".equals(connector.name())) {
                attachingConnector = (AttachingConnector) connector;
                break;
            }
        }

        if (attachingConnector == null) {
            throw new Exception("SocketAttach connector not found");
        }

        // Prepare the connector arguments
        Map<String, Connector.Argument> arguments = attachingConnector.defaultArguments();
        Connector.Argument hostnameArg = arguments.get("hostname");
        hostnameArg.setValue(host); // Set the remote host
        Connector.Argument portArg = arguments.get("port");
        portArg.setValue(Integer.toString(port)); // Set the remote port

        // Attach to the remote JVM
        vm = attachingConnector.attach(arguments);
    }

    @Override
    public void setBreakpoint(String className, int lineNumber) throws Exception {
        EventRequestManager eventManager = vm.eventRequestManager();

        // Locate the class
        List<ReferenceType> classes = vm.classesByName(className);
        if (classes.isEmpty()) {
            throw new Exception("Class not found: " + className);
        }
        ReferenceType classType = classes.get(0);

        // Create a breakpoint request at the specified line
        List<Location> locations = classType.locationsOfLine(lineNumber);
        if (locations.isEmpty()) {
            throw new Exception("No line number " + lineNumber + " in class " + className);
        }
        Location location = locations.get(0);
        BreakpointRequest breakpointRequest = eventManager.createBreakpointRequest(location);
        breakpointRequest.enable();

        EventQueue eventQueue = vm.eventQueue();
        try {
            while (true) {
                EventSet eventSet = eventQueue.remove();
                for (Event event : eventSet) {
                    if (event instanceof BreakpointEvent) {
                        // Handle the breakpoint event asynchronously
                        executorService.execute(() -> handleBreakpointEvent((BreakpointEvent) event));
                    }
                }
                eventSet.resume();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (VMDisconnectedException e) {
            // Remote JVM disconnected or terminated
            System.out.println("Remote JVM disconnected or terminated");
        } finally {
            executorService.shutdown();
            if (vm != null) {
                vm.dispose();
            }
        }

    }

    private void handleBreakpointEvent(BreakpointEvent event) {
        ThreadReference thread = event.thread();
        try {
            thread.suspend();

            // Retrieve the necessary information
            String threadName = thread.name();
            int lineNumber = event.location().lineNumber();
            List<StackFrame> frames = thread.frames();

            // Build the output message
            StringBuilder output = new StringBuilder();
            output.append("Breakpoint hit at line ").append(lineNumber).append("\n");
            output.append("Thread: ").append(threadName).append("\n");

            for (StackFrame stackFrame : frames) {
                // ... retrieve variables and values

                // Append the values of variables to the output
                try {
                    LocalVariable[] variables = stackFrame.visibleVariables().toArray(new LocalVariable[0]);
                    for (LocalVariable variable : variables) {
                        Value value = stackFrame.getValue(variable);
                        output.append("Variable: ")
                              .append(variable.name())
                              .append(", Value: ")
                              .append(value)
                              .append("\n");
                    }
                } catch (InvalidStackFrameException ex) {
                    output.append("    Unable to retrieve value for variables in this stack frame").append("\n");
                } catch (AbsentInformationException e) {
                    output.append("    Absent information for variables in this stack frame").append("\n");
                }
            }

            DebugOutput debugOutput = new DebugOutput(thread, new StringBuilder(output.toString()));
            outputQueue.add(debugOutput);
            executorService.execute(this::processOutputQueue);
        } catch (IncompatibleThreadStateException e) {
            System.out.println("IncompatibleThreadStateException: " + e.getMessage());
        } finally {
            thread.resume();
        }
    }

    private void processOutputQueue() {
        while (!outputQueue.isEmpty()) {
            DebugOutput debugOutput = outputQueue.poll();
            if (debugOutput != null) {
                logThreadOutputs(debugOutput.getThread(), String.valueOf(debugOutput.getOutput()));
            }
        }
    }

    private void logThreadOutputs(ThreadReference thread, String output) {
        logger.info("Thread: " + thread.name() + "\n" + output);
    }

    @Override
    public void disconnect() throws Exception {
        if (vm != null) {
            vm.dispose();
        }
    }
}
