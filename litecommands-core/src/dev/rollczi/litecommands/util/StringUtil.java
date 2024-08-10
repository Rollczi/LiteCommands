package dev.rollczi.litecommands.util;

import java.util.ArrayList;
import java.util.List;

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

    public static List<String> spilt(String text, String delimiter) {
        List<String> list = new ArrayList<>();

        int index = 0;
        int nextIndex;

        while ((nextIndex = text.indexOf(delimiter, index)) != -1) {
            list.add(text.substring(index, nextIndex));
            index = nextIndex + delimiter.length();
        }

        list.add(text.substring(index));
        return list;
    }

    public static List<String> spilt(String text, char delimiter) {
        List<String> list = new ArrayList<>();

        int index = 0;
        int nextIndex;

        while ((nextIndex = text.indexOf(delimiter, index)) != -1) {
            list.add(text.substring(index, nextIndex));
            index = nextIndex + 1;
        }

        list.add(text.substring(index));
        return list;
    }

    public static List<String> splitBySpace(String text) {
        return spilt(text, ' ');
    }

    public static boolean startsWithIgnoreCase(String text, String prefix) {
        return text.regionMatches(true, 0, prefix, 0, prefix.length());
    }

    public static boolean endsWithIgnoreCase(String str, String suffix) {
        int suffixLength = suffix.length();
        return str.regionMatches(true, str.length() - suffixLength, suffix, 0, suffixLength);
    }

    public static int countOf(String sourceContent, char character) {

        int count = 0;
        for (char c : sourceContent.toCharArray()) {
            if (c == character) {
                count++;
            }
        }

        return count;
    }
}
