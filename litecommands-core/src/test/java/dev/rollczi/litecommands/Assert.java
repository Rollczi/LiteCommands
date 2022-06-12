package dev.rollczi.litecommands;

import dev.rollczi.litecommands.command.execute.ExecuteResult;
import org.junit.jupiter.api.Assertions;

import java.util.Collection;
import java.util.stream.Collectors;

public final class Assert {

    private Assert() {}

    public static <T> void assertSize(int expectedSize, Collection<T> actual) {
        Assertions.assertEquals(expectedSize, actual.size());
    }

    public static <T> void assertCollection(Collection<T> expected, Collection<T> actual) {
        for (T t : expected) {
            if (!actual.contains(t)) {
                Assertions.assertEquals(
                        expected.stream().map(Object::toString).collect(Collectors.joining("\n")),
                        actual.stream().map(Object::toString).collect(Collectors.joining("\n"))
                );
            }
        }
    }

    public static <T> void assertCollection(int expectedSize, Collection<T> expected, Collection<T> actual) {
        assertSize(expectedSize, actual);
        assertCollection(expected, actual);
    }

    public static void assertSuccess(ExecuteResult result) {
        Assertions.assertTrue(result.isSuccess());
    }

}
