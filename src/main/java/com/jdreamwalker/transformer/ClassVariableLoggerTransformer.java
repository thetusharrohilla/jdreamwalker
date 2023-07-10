package com.jdreamwalker.transformer;

import javassist.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import static com.jdreamwalker.util.ClassNameCleanerUtil.cleanClassName;

@Slf4j
public class ClassVariableLoggerTransformer implements ClassFileTransformer {

    private final String className;
    private final ClassLoader classLoader;
    private final String method;
    private final String var;
    private final int lineNumber;

    public ClassVariableLoggerTransformer(final String className, final ClassLoader classLoader, String method, String var, int lineNumber) {
        this.className = className;
        this.classLoader = classLoader;
        this.method = method;
        this.var = var;
        this.lineNumber = lineNumber;
    }

    @Override
    public byte[] transform(final ClassLoader loader,
                            final String className,
                            final Class<?> classBeingRedefined,
                            final ProtectionDomain protectionDomain,
                            final byte[] classfileBuffer) throws IllegalClassFormatException {
        final String cleanedClassName = cleanClassName(className);
        if (!this.className.equalsIgnoreCase(cleanedClassName) || !this.classLoader.equals(loader)) {
            return classfileBuffer;
        }
        try {
            final ClassPool classPool = ClassPool.getDefault();
            classPool.insertClassPath(new ClassClassPath(classBeingRedefined));
            final CtClass ctClass = classPool.get(cleanedClassName);
            final CtMethod ctMethod = ctClass.getDeclaredMethod(this.method);
            ctMethod.insertAt(this.lineNumber, String.format("System.out.println(%s);", this.var));
            final byte[] updatedByteCode = ctClass.toBytecode();
            ctClass.detach();
            return updatedByteCode;
        } catch (IOException | CannotCompileException | NotFoundException e) {
            log.error("Error while instrumenting file : {} | Error : {} ",cleanedClassName, e.getMessage(), e);
        }
        return classfileBuffer;

    }
}
