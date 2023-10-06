package dev.rollczi.litecommands.reflect;

import dev.rollczi.litecommands.prettyprint.PrettyPrint;
import dev.rollczi.litecommands.prettyprint.PrettyPrintPicker;

import java.lang.reflect.Executable;
import java.lang.reflect.Parameter;

import static dev.rollczi.litecommands.prettyprint.PrettyPrintClass.*;
import static dev.rollczi.litecommands.prettyprint.PrettyPrintLiteError.*;

public class LiteCommandsReflectInvocationException extends LiteCommandsReflectException {

    public static final String ERROR_PICKER = "^ error: ";

    public LiteCommandsReflectInvocationException(Class<?> clazz, String message) {
        super(formatError(formatClass(clazz, PrettyPrintPicker.CLASS, ERROR_PICKER + message)));
    }

    public LiteCommandsReflectInvocationException(Executable executable, String message) {
        this(executable, null, message, null);
    }

    public LiteCommandsReflectInvocationException(Executable executable, Parameter parameter, String message) {
        this(executable, parameter, message, null);
    }

    public LiteCommandsReflectInvocationException(Executable executable, String message, Throwable cause) {
        super(formatError(PrettyPrint.formatClass(executable, ERROR_PICKER + message)), cause);
    }

    public LiteCommandsReflectInvocationException(Executable executable, Parameter parameter, String message, Throwable cause) {
        super(formatError(PrettyPrint.formatClass(executable, parameter, ERROR_PICKER + message)), cause);
    }

}
