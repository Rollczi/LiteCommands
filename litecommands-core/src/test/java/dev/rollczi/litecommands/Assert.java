package dev.rollczi.litecommands;

import org.junit.jupiter.api.Assertions;

import java.util.Collection;
import java.util.stream.Collectors;

public final class Assert {

    private Assert() {}

    public static <T> void assertCollection(Collection<T> expected, Collection<T> actual) {
        assertCollection(expected, actual, false);
    }

    public static <T> void assertCollection(Collection<T> expected, Collection<T> actual, boolean ignoreSize) {
        if (!ignoreSize && expected.size() != actual.size()) {
            Assertions.assertEquals(Assert.getCollectionString(expected), Assert.getCollectionString(actual));
        }

        for (T t : expected) {
            if (actual.contains(t)) {
                continue;
            }

            Assertions.assertEquals(Assert.getCollectionString(expected), Assert.getCollectionString(actual));
        }
    }

    public static <T> void assertSize(int expectedSize, Collection<T> actual) {
        Assertions.assertEquals(expectedSize, actual.size());
    }

    private static String getCollectionString(Collection<?> collection) {
        return collection.stream().map(Object::toString).collect(Collectors.joining(", "));
    }

}
