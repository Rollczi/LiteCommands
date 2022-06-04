package dev.rollczi.litecommands;

import panda.utilities.text.Joiner;

import java.util.Collection;

public final class Assert {

    private Assert() {}

    public static <T> void assertSize(int expectedSize, Collection<T> actual) {
        if (actual.size() != expectedSize) {
            throw new AssertException(actual.size(), expectedSize);
        }
    }

    public static <T> void assertCollection(Collection<T> expected, Collection<T> actual) {
        for (T t : expected) {
            if (!actual.contains(t)) {
                throw new AssertException(Joiner.on(", ").join(actual), Joiner.on(", ").join(actual));
            }
        }
    }

    public static <T> void assertCollection(int expectedSize, Collection<T> expected, Collection<T> actual) {
        assertSize(expectedSize, actual);
        assertCollection(expected, actual);
    }

}
