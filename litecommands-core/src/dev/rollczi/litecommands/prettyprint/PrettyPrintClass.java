package dev.rollczi.litecommands.prettyprint;

import static dev.rollczi.litecommands.prettyprint.PrettyPrint.NEW_LINE;
import static dev.rollczi.litecommands.prettyprint.PrettyPrint.SPACE;

public final class PrettyPrintClass {

    /** Keyword patterns */
    private static final String CLASS_KEYWORD = "class";
    private static final String INTERFACE_KEYWORD = "interface";
    private static final String ENUM_KEYWORD = "enum";
    private static final String ANNOTATION_KEYWORD = "@interface";

    /** Full Class/Interface/Enum/Annotation pattern */
    private static final String CLASS_PATTERN =
        "{annotations}{modifiers}{keyword} {name}{generic} {" + NEW_LINE +
            "{indentedContent}" + NEW_LINE +
            "}";

    public static String formatClass(Class<?> classToFormat, PrettyPrintPicker picker, String content) {
        String annotations = PrettyPrintAnnotation.formatAnnotations(classToFormat.getAnnotations(), NEW_LINE, NEW_LINE);
        String modifiers = PrettyPrintModifier.formatModifiers(classToFormat.getModifiers()) + SPACE;
        String keyword = formatKeyword(classToFormat);
        String name = classToFormat.getSimpleName();
        String generic = PrettyPrintType.formatGeneric(classToFormat.getTypeParameters());

        int additional = picker == PrettyPrintPicker.CLASS
            ? modifiers.length() + keyword.length() + SPACE.length()
            : 0;

        String indentedContent = PrettyPrintIndent.indentedContent(content, additional);

        return CLASS_PATTERN
            .replace("{annotations}", annotations)
            .replace("{modifiers}", modifiers)
            .replace("{keyword}", keyword)
            .replace("{name}", name)
            .replace("{generic}", generic)
            .replace("{indentedContent}", indentedContent);
    }

    private static String formatKeyword(Class<?> clazz) {
        if (clazz.isInterface()) {
            return INTERFACE_KEYWORD;
        }

        if (clazz.isEnum()) {
            return ENUM_KEYWORD;
        }

        if (clazz.isAnnotation()) {
            return ANNOTATION_KEYWORD;
        }

        return CLASS_KEYWORD;
    }

}
