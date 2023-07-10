package com.jdreamwalker.service;

import com.jdreamwalker.transformer.ClassVariableLoggerTransformer;
import lombok.extern.slf4j.Slf4j;

import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.util.Objects;

@Slf4j
public class VariableLogAdditionService {

    private final Instrumentation instrumentation;

    public VariableLogAdditionService(final Instrumentation instrumentation) {
        this.instrumentation = instrumentation;
    }

    public boolean addVariableLogLine(final String className, final String method, final int lineNumber, final String variableToLog) {
        Class<?> loadedClass = null;
        try {
            loadedClass = Class.forName(className);
        } catch (ClassNotFoundException e) {
            log.warn("Class : {} not found using Class.forName", className);
        }

        if (Objects.isNull(loadedClass)) {
            for (final Class<?> classFromJvm : instrumentation.getAllLoadedClasses()) {
                if (classFromJvm.getName().equalsIgnoreCase(className)) {
                    loadedClass = classFromJvm;
                }
            }
        }

        if (Objects.isNull(loadedClass)) {
            log.warn("Unable to locate class : {} for instrumentation, skipping", className);
            return false;
        }
        try {
            return transform(loadedClass, loadedClass.getClassLoader(), method, variableToLog, lineNumber);
        } catch (final UnmodifiableClassException e) {
            log.error("Unable to instrument classes : {} | Error : {}", className, e.getMessage(), e);
        }
        return false;
    }

    private boolean transform(final Class<?> clazz,
                              final ClassLoader classLoader,
                              final String method,
                              final String var,
                              final int lineNumber) throws UnmodifiableClassException {
        final ClassVariableLoggerTransformer classVariableLoggerTransformer =
                new ClassVariableLoggerTransformer(clazz.getName(), classLoader, method, var, lineNumber);
        instrumentation.addTransformer(classVariableLoggerTransformer, true);
        instrumentation.retransformClasses(clazz);
        return true;
    }

}
