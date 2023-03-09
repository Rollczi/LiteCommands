package dev.rollczi.litecommands.modern.test;

import dev.rollczi.litecommands.modern.invocation.Invocation;

public final class TestUtil {

    public static Invocation<FakeSender> invocation(String command, String... args) {
        return new Invocation<>(new FakeSender(), new FakePlatformSender(), command, command, args);
    }

}
