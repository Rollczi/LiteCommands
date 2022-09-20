package dev.rollczi.litecommands;

import dev.rollczi.litecommands.command.LiteInvocation;

import java.util.Arrays;
import java.util.List;

public final class TestUtils {

    private TestUtils() {}

    @SafeVarargs
    public static <T> List<T> list(T... elements) {
        return Arrays.asList(elements);
    }

    public static LiteInvocation invocation() {
        return invocation("test");
    }

    public static LiteInvocation invocation(String command, String... args) {
        return new LiteInvocation(new TestSender(new TestHandle()), command, command, args);
    }

}

