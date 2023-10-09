package dev.rollczi.litecommands.prettyprint;

import java.lang.reflect.Modifier;

import static dev.rollczi.litecommands.prettyprint.PrettyPrint.SPACE;

final class PrettyPrintModifier {

    static String formatModifiers(int modifiers) {
        String formatted = Modifier.toString(modifiers);

        if (formatted.isEmpty()) {
            return formatted;
        }

        return formatted + SPACE;
    }

}
