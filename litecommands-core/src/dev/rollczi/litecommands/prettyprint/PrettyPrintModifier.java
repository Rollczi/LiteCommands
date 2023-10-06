package dev.rollczi.litecommands.prettyprint;

import java.lang.reflect.Modifier;

final class PrettyPrintModifier {

    static String formatModifiers(int modifiers) {
        return Modifier.toString(modifiers);
    }

}
