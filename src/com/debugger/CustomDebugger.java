package com.debugger;

import com.sun.jdi.AbsentInformationException;
import com.sun.jdi.ArrayReference;
import com.sun.jdi.Bootstrap;
import com.sun.jdi.Field;
import com.sun.jdi.IncompatibleThreadStateException;
import com.sun.jdi.LocalVariable;
import com.sun.jdi.Location;
import com.sun.jdi.ObjectReference;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.StackFrame;
import com.sun.jdi.StringReference;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CustomDebugger implements Debugger {
    private final ExecutorService executorService;

    private VirtualMachine vm;

    public CustomDebugger() {
        // Initialize the executor service with a single thread
        executorService = Executors.newSingleThreadExecutor();
    }

    @Override
    public void connect(String host, int port) throws Exception {
        VirtualMachineManager vmManager = Bootstrap.virtualMachineManager();
        AttachingConnector attachingConnector = null;
        for (Connector connector: vmManager.attachingConnectors()) {
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
                for (Event event: eventSet) {
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
        System.out.println("Breakpoint hit at line " + event.location().lineNumber());

        try {
            ThreadReference thread = event.thread();

            StackFrame frame = thread.frame(0);
            List<StackFrame> frames = thread.frames();

            for (StackFrame stackFrame: frames) {
                Location location = stackFrame.location();
                try {
                    // Retrieve and print the values of variables
                    LocalVariable[] variables = stackFrame.visibleVariables().toArray(new LocalVariable[0]);
                    for (LocalVariable variable: variables) {
                        Value value = stackFrame.getValue(variable);
                        String json = getVariableJson(variable, value);
                        System.out.println("Variable: " + variable.name() + ", Value: " + json);
                    }
                } catch (AbsentInformationException ex) {
                    System.out.println("    Unable to retrieve variable information for this stack frame");
                }
            }
        } catch (IncompatibleThreadStateException e) {
            System.out.println("IncompatibleThreadStateException: " + e.getMessage());
        }
    }

    private String getVariableJson(LocalVariable variable, Value value) {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{");
        jsonBuilder.append("\"name\":").append("\"").append(variable.name()).append("\"");
        jsonBuilder.append(",");
        jsonBuilder.append("\"type\":").append("\"").append(variable.typeName()).append("\"");
        jsonBuilder.append(",");
        jsonBuilder.append("\"value\":").append(getValueJson(value));
        // Add more fields as needed
        jsonBuilder.append("}");
        return jsonBuilder.toString();
    }

    private String getValueJson(Value value) {
        if (value == null) {
            return "null";
        } else if (value instanceof StringReference) {
            return "\"" + ((StringReference) value).value() + "\"";
        } else if (value instanceof ArrayReference) {
            ArrayReference arrayReference = (ArrayReference) value;
            List<Value> arrayValues = arrayReference.getValues();
            StringBuilder jsonBuilder = new StringBuilder();
            jsonBuilder.append("[");
            for (int i = 0; i < arrayValues.size(); i++) {
                Value arrayValue = arrayValues.get(i);
                jsonBuilder.append(getValueJson(arrayValue));
                if (i < arrayValues.size() - 1) {
                    jsonBuilder.append(",");
                }
            }
            jsonBuilder.append("]");
            return jsonBuilder.toString();
        } else if (value instanceof ObjectReference) {
            ObjectReference objectReference = (ObjectReference) value;
            StringBuilder jsonBuilder = new StringBuilder();
            jsonBuilder.append("{");
            List<Field> fields = objectReference.referenceType().allFields();
            for (int i = 0; i < fields.size(); i++) {
                Field field = fields.get(i);
                Value fieldValue = objectReference.getValue(field);
                jsonBuilder.append("\"").append(field.name()).append("\":").append(getValueJson(fieldValue));
                if (i < fields.size() - 1) {
                    jsonBuilder.append(",");
                }
            }
            jsonBuilder.append("}");
            return jsonBuilder.toString();
        } else {
            return value.toString();
        }
    }

    @Override
    public void disconnect() throws Exception {
        if (vm != null) {
            vm.dispose();
        }
    }
}
