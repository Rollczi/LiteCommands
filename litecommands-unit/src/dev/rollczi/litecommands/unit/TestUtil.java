package dev.rollczi.litecommands.unit;

import dev.rollczi.litecommands.argument.input.ArgumentsInput;
import dev.rollczi.litecommands.invocation.Invocation;

public final class TestUtil {

    private TestUtil() {
    }

    public static Invocation<TestSender> invocation(String command, String... args) {
        return new Invocation<>(new TestSender(), new TestPlatformSender(), command, command, ArgumentsInput.raw(args));
    }

}
