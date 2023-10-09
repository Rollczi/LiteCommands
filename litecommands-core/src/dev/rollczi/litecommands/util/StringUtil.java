package dev.rollczi.litecommands.util;

public final class StringUtil {

    public static final String EMPTY = "";

    private StringUtil() {}

    public static String repeat(String text, int length) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < length; i++) {
            builder.append(text);
        }

        return builder.toString();
    }

}
