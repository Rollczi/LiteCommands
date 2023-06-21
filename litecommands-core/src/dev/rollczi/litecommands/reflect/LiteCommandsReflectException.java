package dev.rollczi.litecommands.reflect;

import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.exception.LiteCommandsException;

import java.lang.reflect.Executable;
import java.lang.reflect.Parameter;

public class LiteCommandsReflectException extends LiteCommandsException {

    private static final String NEW_LINE = System.lineSeparator();
    private static final String SEPARATOR = "------------------------------------------------------------------------------------";

    public LiteCommandsReflectException(Class<?> clazz, String message) {
        super(buildClassMessage(clazz, message));
    }

    public LiteCommandsReflectException(Executable executable, String message) {
        this(executable, null, message, null);
    }

    public LiteCommandsReflectException(Executable executable, Parameter parameter, String message) {
        this(executable, parameter, message, null);
    }

    public LiteCommandsReflectException(Executable executable, String message, Throwable cause) {
        this(executable, null, message, cause);
    }

    public LiteCommandsReflectException(Executable executable, Parameter parameter, String message, Throwable cause) {
        super(buildExecutableMessage(executable, parameter, message), cause);
    }

    private static String buildClassMessage(Class<?> clazz, String message) {
        return NEW_LINE + SEPARATOR +
            NEW_LINE + " LiteCommands: " + LiteCommands.VERSION +
            NEW_LINE + " error: " +  message +
            NEW_LINE + " file: " + fileName(clazz) +
            NEW_LINE + SEPARATOR +
            NEW_LINE + ReflectFormatUtil.classPick(clazz, message) +
            NEW_LINE + SEPARATOR;
    }

    private static String buildExecutableMessage(Executable executable, Parameter parameter, String message) {
        return NEW_LINE + SEPARATOR +
            NEW_LINE + " LiteCommands: " + LiteCommands.VERSION +
            NEW_LINE + " error: " +  message +
            NEW_LINE + " file: " + fileName(executable.getDeclaringClass()) +
            NEW_LINE + SEPARATOR +
            NEW_LINE + ReflectFormatUtil.executablePick(executable, parameter, message) +
            NEW_LINE + SEPARATOR;
    }

    private static String fileName(Class<?> clazz) {
        Class<?> currentClass = clazz;

        while (currentClass.getEnclosingClass() != null) {
            currentClass = currentClass.getEnclosingClass();
        }

        return currentClass.getName() + ".java";
    }

}
