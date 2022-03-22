package dev.rollczi.litecommands.utils;

import panda.utilities.text.Joiner;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public final class ReflectUtils {

    private ReflectUtils() {}

    public static String formatClass(Class<?> clazz) {
        return clazz.getSimpleName();
    }

    public static String formatMethod(Method method) {
        return formatClass(method.getDeclaringClass()) + "#" + method.getName();
    }

    public static String formatMethodParams(Method method) {
        return formatMethod(method) + "(" + formatParams(method) + ")";
    }

    public static String formatParams(Method method) {
        return Joiner.on(", ").join(method.getParameters(), param -> {
            String annotations = Joiner.on(" ").join(param.getAnnotations(), annotation -> "@" + annotation.annotationType().getSimpleName()).toString();
            String type = param.getType().getSimpleName();
            String name = param.getName();

            return annotations.isEmpty() ? type + " " + name : annotations + " " + type + " " + name;
        }).toString();
    }

    public static String modifier(Method method) {
        return Modifier.toString(method.getModifiers());
    }

}
