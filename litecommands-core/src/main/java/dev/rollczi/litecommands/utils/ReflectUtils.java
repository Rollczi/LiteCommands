package dev.rollczi.litecommands.utils;

import panda.utilities.text.Joiner;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class ReflectUtils {

    public static String formatMethod(Method method) {
        return method.getDeclaringClass().getSimpleName() + "#" + method.getName();
    }

    public static String formatMethodParams(Method method) {
        return formatMethod(method) + "(" + formatParams(method) + ")";
    }

    public static String formatParams(Method method) {
        return Joiner.on(", ").join(method.getParameters(), param -> param.getType().getSimpleName() + " " + param.getName()).toString();
    }

    public static String modifier(Method method) {
        return Modifier.toString(method.getModifiers());
    }

}
