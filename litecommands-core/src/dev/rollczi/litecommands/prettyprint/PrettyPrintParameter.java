package dev.rollczi.litecommands.prettyprint;

import java.lang.annotation.Annotation;
import java.lang.reflect.Executable;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.WeakHashMap;

import static dev.rollczi.litecommands.prettyprint.PrettyPrint.NEW_INLINE;
import static dev.rollczi.litecommands.prettyprint.PrettyPrint.SPACE;

public final class PrettyPrintParameter {

    /** Parameter patterns */
    private static final String PARAMETER_PATTERN = "{annotations}{type} {name}";

    /** Cache for reduce reflection calls */
    private static final Map<Parameter, Annotation[]> parameterAnnotationsCache = new WeakHashMap<>();

    static List<String> formatParameters(Parameter[] parameters) {
        List<String> list = new ArrayList<>();

        for (Parameter parameter : parameters) {
            list.add(formatParameter(parameter));
        }

        return list;
    }

    static String formatParameters(List<String> formatParameters) {
        StringJoiner joiner = new StringJoiner(NEW_INLINE);

        for (String parameter : formatParameters) {
            joiner.add(parameter);
        }

        return joiner.toString();
    }

    static int getParameterIndex(Executable method, Parameter parameter) {
        Parameter[] parameters = method.getParameters();

        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i].equals(parameter)) {
                return i;
            }
        }

        return -1;
    }

    public static String formatParameter(Parameter parameter) {
        Annotation[] parameterAnnotations = parameterAnnotationsCache.computeIfAbsent(parameter, key -> parameter.getAnnotations());

        String annotations = PrettyPrintAnnotation.formatAnnotations(parameterAnnotations, NEW_INLINE, SPACE);
        String type = PrettyPrintType.formatType(parameter.getParameterizedType());
        String name = parameter.getName();

        return PARAMETER_PATTERN
            .replace("{annotations}", annotations)
            .replace("{type}", type)
            .replace("{name}", name);
    }


}
