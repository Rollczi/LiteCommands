package dev.rollczi.litecommands.shared;

import panda.std.function.ThrowingSupplier;

import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.stream.Collectors;

public final class ReflectFormat {

    private static final String METHOD = "%s#%s";
    private static final String METHOD_WITH_PARAM = "%s#%s(%s)";
    private static final String METHOD_WITH_ALL = "%s %s %s(%s)";

    private ReflectFormat() {}

    public static String singleClass(Class<?> clazz) {
        return clazz.getSimpleName();
    }

    public static String docksShortExecutable(Executable executable) {
        return String.format(METHOD, singleClass(executable.getDeclaringClass()), executable.getName());
    }

    public static String docsExecutable(Executable executable) {
        return String.format(METHOD_WITH_PARAM, singleClass(
                executable.getDeclaringClass()),
                executable instanceof Constructor ? executable.getDeclaringClass().getSimpleName() : executable.getName(),
                parameters(executable)
        );
    }

    public static String method(Method method) {
        return String.format(METHOD_WITH_ALL, modifier(method), singleClass(method.getReturnType()), method.getName(), parameters(method));
    }

    public static String method(ThrowingSupplier<Method, ReflectiveOperationException> supplier) {
        try {
            return method(supplier.get());
        } catch (ReflectiveOperationException argumentException) {
            throw new IllegalArgumentException(argumentException);
        }
    }

    public static String parameters(Executable executable) {
        return Arrays.stream(executable.getParameters())
            .map(ReflectFormat::parameter)
            .collect(Collectors.joining(", "));
    }

    public static String parameter(Parameter parameter) {
        String annotations = Arrays.stream(parameter.getAnnotations()).map(annotation -> "@" + annotation.annotationType().getSimpleName()).collect(Collectors.joining(" "));
        String type = parameter.getType().getSimpleName();
        String name = parameter.getName();

        return annotations.isEmpty() ? type + " " + name : annotations + " " + type + " " + name;
    }

    public static String modifier(Method method) {
        return Modifier.toString(method.getModifiers());
    }

}
