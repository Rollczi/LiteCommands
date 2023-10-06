package dev.rollczi.litecommands.prettyprint;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import static dev.rollczi.litecommands.prettyprint.PrettyPrint.NEW_LINE;
import static dev.rollczi.litecommands.prettyprint.PrettyPrint.SPACE;

public final class PrettyPrintMethod {

    /** Full Method patterns */
    private static final String METHOD_PATTERN =
        "{annotations}{modifiers}{generic}{type} {name}({parameters}) {" + NEW_LINE +
            "{indentedContent}" + NEW_LINE +
            "}";

    /** Parameter patterns */
    private static final String PARAMETER_PATTERN = "{annotations}{type} {name}";

    /** Cache for reduce reflection calls */
    private static final Map<Parameter, Annotation[]> parameterAnnotationsCache = new WeakHashMap<>();

    public static String formatMethod(Method method, PrettyPrintPicker picker, String content) {
        return formatMethod(method, -1, picker, content);
    }

    public static String formatMethod(Method method, Parameter parameter, String content) {
        return formatMethod(method, PrettyPrintParameter.getParameterIndex(method, parameter), PrettyPrintPicker.PARAMETER, content);
    }

    private static String formatMethod(Method method, int parameter, PrettyPrintPicker picker, String content) {
        String annotations = PrettyPrintAnnotation.formatAnnotations(method.getAnnotations(), NEW_LINE, NEW_LINE);
        String modifiers = PrettyPrintModifier.formatModifiers(method.getModifiers());
        String generic = PrettyPrintType.formatGeneric(method.getTypeParameters());
        String type = PrettyPrintType.formatType(method.getGenericReturnType());
        String name = method.getName();
        List<String> formattedParameters = PrettyPrintParameter.formatParameters(method.getParameters());
        String parameters = PrettyPrintParameter.formatParameters(formattedParameters);

        int additional = 0;

        if (picker == PrettyPrintPicker.EXECUTABLE) {
            additional = modifiers.length() + generic.length() + type.length() + SPACE.length();
        }

        if (picker == PrettyPrintPicker.PARAMETER && parameter != -1) {
            String pickedParameters = PrettyPrintParameter.formatParameters(formattedParameters.subList(0, parameter));

            additional = modifiers.length() + generic.length() + type.length() + SPACE.length() + name.length() + pickedParameters.length() + SPACE.length();
        }

        String indentedContent = PrettyPrintIndent.indentedContent(content, additional);

        return METHOD_PATTERN
            .replace("{annotations}", annotations)
            .replace("{modifiers}", modifiers)
            .replace("{generic}", generic)
            .replace("{type}", type)
            .replace("{name}", name)
            .replace("{parameters}", parameters)
            .replace("{indentedContent}", indentedContent);
    }

}
