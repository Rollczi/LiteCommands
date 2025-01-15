package dev.rollczi.litecommands.unit;

import dev.rollczi.litecommands.argument.parser.input.ParseableInput;
import dev.rollczi.litecommands.invocation.Invocation;

public final class TestUtil {

    private TestUtil() {
    }

    public static Invocation<TestSender> invocation(String command, String... args) {
        return new Invocation<>(new TestSender(), new TestPlatformSender(), command, command, ParseableInput.raw(args));
    }

    public static <SENDER> Invocation<SENDER> invocation(SENDER sender, String command, String... args) {
        return new Invocation<>(sender, new TestPlatformSender(), command, command, ParseableInput.raw(args));
    }

}
