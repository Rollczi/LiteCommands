package dev.rollczi.litecommands.prettyprint;

import dev.rollczi.litecommands.util.ObjectsUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.Map;
import java.util.StringJoiner;
import java.util.WeakHashMap;

final class PrettyPrintAnnotation {

    /** Annotation patterns */
    private static final String ANNOTATION_PATTERN = "@{name}({parameters})";
    private static final String ANNOTATION_EMPTY_PATTERN = "@{name}";
    private static final String ANNOTATION_PARAMETER_PATTERN = "{name} = {value}";

    /** Annotation value patterns */
    private static final String ARRAY_VALUE_PATTERN = "{ {value} }";
    private static final String STRING_VALUE_START = "\"";
    private static final String STRING_VALUE_END = "\"";
    private static final String CLASS_VALUE_PATTERN = "{value}.class";
    private static final String ENUM_VALUE_PATTERN = "{enumClass}.{value}";

    /** Cache for reduce reflection calls */
    private static final Map<Method, Object> methodAnnotationsDefaultValuesCache = new WeakHashMap<>();
    private static final Map<Class<? extends Annotation>, Method[]> annotationAttributesCache = new WeakHashMap<>();

    static String formatAnnotations(Annotation[] annotations, String separator, String endSeparator) {
        StringJoiner builder = new StringJoiner(separator);

        for (Annotation annotation : annotations) {
            builder.add(formatAnnotation(annotation));
        }

        return builder + endSeparator;
    }

    private static String formatAnnotation(Annotation annotation) {
        String name = annotation.annotationType().getSimpleName();
        String parameters = formatAnnotationParameters(annotation);

        if (parameters.isEmpty()) {
            return ANNOTATION_EMPTY_PATTERN.replace("{name}", name);
        }

        return ANNOTATION_PATTERN
            .replace("{name}", name)
            .replace("{parameters}", parameters);
    }

    private static String formatAnnotationParameters(Annotation annotation) {
        StringJoiner joiner = new StringJoiner(PrettyPrint.NEW_INLINE);
        Method[] methods = annotationAttributesCache.computeIfAbsent(annotation.annotationType(), key -> annotation.annotationType().getDeclaredMethods());

        for (Method method : methods) {
            try {
                Object value = method.invoke(annotation);
                Object defaultValue = methodAnnotationsDefaultValuesCache.computeIfAbsent(method, key -> method.getDefaultValue());

                if (ObjectsUtil.equals(value, defaultValue)) {
                    continue;
                }

                String formattedValue = formatAnnotationParameterValue(value);
                String formattedParameter = ANNOTATION_PARAMETER_PATTERN
                    .replace("{name}", method.getName())
                    .replace("{value}", formattedValue);

                joiner.add(formattedParameter);
            }
            catch (InvocationTargetException | IllegalAccessException ignore) {
            }
        }

        return joiner.toString();
    }

    private static String formatAnnotationParameterValue(Object value) {
        if (value instanceof String) {
            return STRING_VALUE_START + value + STRING_VALUE_END;
        }

        if (value instanceof Class) {
            Class<?> aClass = (Class<?>) value;

            return CLASS_VALUE_PATTERN
                .replace("{value}", aClass.getSimpleName());
        }

        if (value instanceof Enum) {
            Enum<?> anEnum = (Enum<?>) value;

            return ENUM_VALUE_PATTERN
                .replace("{enumClass}", anEnum.getDeclaringClass().getSimpleName())
                .replace("{value}", anEnum.name());
        }

        if (value instanceof Annotation) {
            return formatAnnotation((Annotation) value);
        }

        if (value instanceof Object[]) {
            StringJoiner joiner = new StringJoiner(PrettyPrint.NEW_INLINE);

            for (Object object : (Object[]) value) {
                joiner.add(formatAnnotationParameterValue(object));
            }

            return ARRAY_VALUE_PATTERN.replace("{value}", joiner.toString());
        }

        return value.toString();
    }

}
