package dev.rollczi.litecommands.prettyprint;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.List;

import static dev.rollczi.litecommands.prettyprint.PrettyPrint.NEW_LINE;
import static dev.rollczi.litecommands.prettyprint.PrettyPrint.SPACE;

public final class PrettyPrintConstructor {

    private static final String CONSTRUCTOR_PATTERN =
        "{annotations}{modifiers}{generic}{name}({parameters}) {" + NEW_LINE +
        "{indentedContent}" + NEW_LINE +
        "}";

    public static String formatMethod(Constructor<?> method, PrettyPrintPicker picker, String content) {
        return formatMethod(method, -1, picker, content);
    }

    public static String formatMethod(Constructor<?> method, Parameter parameter, String content) {
        return formatMethod(method, PrettyPrintParameter.getParameterIndex(method, parameter), PrettyPrintPicker.PARAMETER, content);
    }

    private static String formatMethod(Constructor<?> method, int parameter, PrettyPrintPicker picker, String content) {
        String annotations = PrettyPrintAnnotation.formatAnnotations(method.getAnnotations(), NEW_LINE, NEW_LINE);
        String modifiers = PrettyPrintModifier.formatModifiers(method.getModifiers()) + SPACE;
        String generic = PrettyPrintType.formatGeneric(method.getTypeParameters()) + SPACE;
        String name = method.getName();
        List<String> formattedParameters = PrettyPrintParameter.formatParameters(method.getParameters());
        String parameters = PrettyPrintParameter.formatParameters(formattedParameters);

        int additional = 0;

        if (picker == PrettyPrintPicker.EXECUTABLE) {
            additional = modifiers.length() + generic.length();
        }

        if (picker == PrettyPrintPicker.PARAMETER && parameter != -1) {
            String pickedParameters = PrettyPrintParameter.formatParameters(formattedParameters.subList(0, parameter));

            additional = modifiers.length() + generic.length() + name.length() + pickedParameters.length() + SPACE.length();
        }

        String indentedContent = PrettyPrintIndent.indentedContent(content, additional);

        return CONSTRUCTOR_PATTERN
            .replace("{annotations}", annotations)
            .replace("{modifiers}", modifiers)
            .replace("{generic}", generic)
            .replace("{name}", name)
            .replace("{parameters}", parameters)
            .replace("{indentedContent}", indentedContent);
    }

}
