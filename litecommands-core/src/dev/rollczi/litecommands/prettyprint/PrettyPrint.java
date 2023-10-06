package dev.rollczi.litecommands.prettyprint;

import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public final class PrettyPrint {

    /** Whitespace patterns */
    static final String SPACE = " ";
    static final String NEW_LINE = "\n";
    static final String NEW_INLINE = ", ";

    public static String formatClass(Executable executable, Parameter parameter, String content) {
        String formatted = executable instanceof Constructor
                ? PrettyPrintConstructor.formatConstructor((Constructor<?>) executable, parameter, content)
                : PrettyPrintMethod.formatMethod((Method) executable, parameter, content);

        return PrettyPrintClass.formatClass(executable.getDeclaringClass(), PrettyPrintPicker.NONE, NEW_LINE + formatted);
    }

    public static String formatClass(Executable executable, String content) {
        String formatted = executable instanceof Constructor
                ? PrettyPrintConstructor.formatConstructor((Constructor<?>) executable, PrettyPrintPicker.EXECUTABLE, content)
                : PrettyPrintMethod.formatMethod((Method) executable, PrettyPrintPicker.EXECUTABLE, content);

        return PrettyPrintClass.formatClass(executable.getDeclaringClass(), PrettyPrintPicker.NONE, NEW_LINE + formatted);
    }

}
