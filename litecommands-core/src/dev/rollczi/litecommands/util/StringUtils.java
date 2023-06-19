package dev.rollczi.litecommands.util;

public final class StringUtils {

    public static final String EMPTY = "";

    private StringUtils() {}

    public static String repeat(String text, int length) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < length; i++) {
            builder.append(text);
        }

        return builder.toString();
    }

}
