package dev.rollczi.litecommands;

import java.util.Arrays;
import java.util.List;

public final class TestUtils {

    private TestUtils() {}

    @SafeVarargs
    public static <T> List<T> list(T... elements) {
        return Arrays.asList(elements);
    }

}

