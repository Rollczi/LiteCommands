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

    public static boolean startsWithIgnoreCase(String text, String prefix) {
        return text.regionMatches(true, 0, prefix, 0, prefix.length());
    }

    public static boolean endsWithIgnoreCase(String str, String suffix) {
        int suffixLength = suffix.length();
        return str.regionMatches(true, str.length() - suffixLength, suffix, 0, suffixLength);
    }

}
