package dev.rollczi.litecommands.reflect;

import dev.rollczi.litecommands.util.ObjectsUtil;
import dev.rollczi.litecommands.util.StringUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class ReflectFormatUtil {

    private static final String TAB = "    ";

    private static final String[] CLASS_PICK = {
        "%s {", // class name
        "%s", // picker
        "}",
    };

    private static final String[] EXECUTABLE_PICK = {
        "%s {", // class name
        "    %s", // executable annotation
        "    %s {", // executable
        "    %s", // picker
        "",
        "}",
    };

    private static final String PICKER_SYMBOL = "^";
    private static final String PICKER_MESSAGE = " -> %s";

    private static final String METHOD_WITH_ALL = "%s %s %s(%s)";
    private static final String CONSTRUCTOR_WITH_ALL = "%s %s(%s)";
    private static final String CLASS_WITH_ALL = "%s class %s";
    private static final String ANNOTATION = "@%s";
    private static final String ANNOTATION_WITH_VALUE = "@%s(%s)";

    private ReflectFormatUtil() {}

    public static String singleClass(Class<?> clazz) {
        return clazz.getSimpleName();
    }

    public static String fullClass(Class<?> clazz) {
        return String.format(CLASS_WITH_ALL, modifier(clazz.getModifiers()), clazz.getSimpleName());
    }

    /**
     * Format: [modifier] [return type] [method name]([parameters])
     * Example: public String method(@Arg String arg, int index)
     *
     * @param method method to format
     * @return formatted method
     */
    public static String method(Method method) {
        return String.format(METHOD_WITH_ALL, modifier(method), singleClass(method.getReturnType()), method.getName(), parameters(method))
            .trim();
    }

    public static String constructor(Constructor<?> constructor) {
        return String.format(CONSTRUCTOR_WITH_ALL, modifier(constructor), constructor.getName(), parameters(constructor))
            .trim();
    }

    public static String executable(Executable executable) {
        return executable instanceof Method ? method((Method) executable) : constructor((Constructor<?>) executable);
    }

    static String executablePick(Executable executable, String message) {
        return executablePick(executable, null, message);
    }

    static String classPick(Class<?> clazz, String message) {
        String className = fullClass(clazz);
        String classAnnotations = Arrays.stream(clazz.getAnnotations())
            .map(annotation ->  annotation(annotation) + "\n")
            .collect(Collectors.joining());
        String finalClassName = classAnnotations + className;

        int nameLength = singleClass(clazz).length();

        String pickerMessage = String.format(PICKER_MESSAGE, message);
        String fullPicker = StringUtil.repeat(" ", className.length() - nameLength) + StringUtil.repeat(PICKER_SYMBOL, nameLength) + pickerMessage;

        return String.format(String.join("\n", CLASS_PICK), finalClassName, fullPicker);
    }

    static String executablePick(Executable executable, Parameter parameter, String message) {

        String className = fullClass(executable.getDeclaringClass());
        String classAnnotations = Arrays.stream(executable.getDeclaringClass().getAnnotations())
            .map(annotation ->  annotation(annotation) + "\n")
            .collect(Collectors.joining());
        className = classAnnotations + className;

        String executableFormat = executable(executable);
        String executableAnnotations = Arrays.stream(executable.getAnnotations())
            .map(annotation ->  "\n" + TAB + annotation(annotation))
            .collect(Collectors.joining());

        String pickerMessage = String.format(PICKER_MESSAGE, message);
        String fullPicker = StringUtil.repeat(PICKER_SYMBOL, executableFormat.length()) + pickerMessage;

        if (parameter != null) {
            String parameterFormat = parameter(parameter);
            int currentParameter = executableFormat.indexOf(parameterFormat);

            if (currentParameter != -1) {
                String pickerSymbols = StringUtil.repeat(PICKER_SYMBOL, parameterFormat.length());

                fullPicker = StringUtil.repeat(" ", currentParameter) + pickerSymbols + pickerMessage;
            }
        }

        return String.format(String.join("\n", EXECUTABLE_PICK), className, executableAnnotations, executableFormat, fullPicker);
    }

    public static String parameters(Executable executable) {
        return Arrays.stream(executable.getParameters())
            .map(parameter -> parameter(parameter))
            .collect(Collectors.joining(", "));
    }

    public static String parameter(Parameter parameter) {
        String annotations = Arrays.stream(parameter.getAnnotations())
            .map(annotation -> annotation(annotation))
            .collect(Collectors.joining(" "));

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
        Method[] methods = annotation.annotationType().getDeclaredMethods();

        if (methods.length == 0) {
            return String.format(ANNOTATION, annotation.annotationType().getSimpleName());
        }

        List<String> values = new ArrayList<>();

        for (Method declaredMethod : methods) {
            try {
                Object value = declaredMethod.invoke(annotation);

                if (value == null || ObjectsUtil.equals(value, declaredMethod.getDefaultValue())) {
                    continue;
                }

                values.add(declaredMethod.getName() + " = " + formatValueOfAnnotation(value));
            }
            catch (Exception exception) {
                throw new RuntimeException("Cannot invoke method " + declaredMethod.getName() + " from annotation " + annotation.annotationType().getSimpleName(), exception);
            }
        }

        if (values.isEmpty()) {
            return String.format(ANNOTATION, annotation.annotationType().getSimpleName());
        }

        return String.format(ANNOTATION_WITH_VALUE, annotation.annotationType().getSimpleName(), String.join(", ", values));
    }

    private static String formatValueOfAnnotation(Object value) {
        if (value instanceof String) {
            return  "\"" + value + "\"";
        }

        if (value instanceof Class<?>) {
            return  ((Class<?>) value).getSimpleName() + ".class";
        }

        if (value instanceof Enum<?>) {
            return  ((Enum<?>) value).name();
        }

        if (value instanceof Annotation) {
            return annotation((Annotation) value);
        }

        if (value instanceof Object[]) {
            return Arrays.stream((Object[]) value)
                .map(value1 -> formatValueOfAnnotation(value1))
                .collect(Collectors.joining(", ", "{", "}"));
        }

        return value.toString();
    }

    public static String modifier(Member member) {
        return modifier(member.getModifiers());
    }

    public static String modifier(int modifiers) {
        return Modifier.toString(modifiers);
    }


}
