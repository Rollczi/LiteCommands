package dev.rollczi.litecommands.modern.util;

import panda.std.function.ThrowingSupplier;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.Arrays;
import java.util.stream.Collectors;

public final class ReflectFormatUtil {

    private static final String METHOD_WITH_ALL = "%s %s %s(%s)";
    private static final String ANNOTATION = "@%s";

    private ReflectFormatUtil() {}

    public static String singleClass(Class<?> clazz) {
        return clazz.getSimpleName();
    }

    public static String method(Method method) {
        return String.format(METHOD_WITH_ALL, modifier(method), singleClass(method.getReturnType()), method.getName(), parameters(method));
    }

    public static String parameters(Executable executable) {
        return Arrays.stream(executable.getParameters())
            .map(ReflectFormatUtil::parameter)
            .collect(Collectors.joining(", "));
    }

    public static String parameter(Parameter parameter) {
        String annotations = Arrays.stream(parameter.getAnnotations()).map(ReflectFormatUtil::annotation).collect(Collectors.joining(" "));
        String type = parameter.getType().getSimpleName();
        String name = parameter.getName();

        return annotations.isEmpty() ? type + " " + name : annotations + " " + type + " " + name;
    }

    public static String parameter(Parameter parameter, Annotation annotation) {
        String annotationFormat = annotation(annotation);
        String type = parameter.getType().getSimpleName();
        String name = parameter.getName();

        return annotationFormat + " " + type + " " + name;
    }

    public static String annotation(Annotation annotation) {
        return String.format(ANNOTATION, annotation.annotationType().getSimpleName());
    }

    public static String modifier(Method method) {
        return Modifier.toString(method.getModifiers());
    }

}
