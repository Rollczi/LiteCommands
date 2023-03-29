package dev.rollczi.litecommands.unit;

import dev.rollczi.litecommands.invocation.Invocation;

public final class TestUtil {

    public static Invocation<TestSender> invocation(String command, String... args) {
        return new Invocation<>(new TestSender(), new TestPlatformSender(), command, command, args);
    }

}
