package dev.rollczi.litecommands;

import java.util.Collection;
import java.util.stream.Collectors;

public final class Assert {

    private Assert() {}

    public static <T> void assertSize(int expectedSize, Collection<T> actual) {
        if (actual.size() != expectedSize) {
            throw new AssertException(expectedSize, actual.size());
        }
    }

    public static <T> void assertCollection(Collection<T> expected, Collection<T> actual) {
        for (T t : expected) {
            if (!actual.contains(t)) {
                throw new AssertException(
                        expected.stream().map(Object::toString).collect(Collectors.joining(", ")),
                        actual.stream().map(Object::toString).collect(Collectors.joining(", "))
                );
            }
        }
    }

    public static <T> void assertCollection(int expectedSize, Collection<T> expected, Collection<T> actual) {
        assertSize(expectedSize, actual);
        assertCollection(expected, actual);
    }

}
