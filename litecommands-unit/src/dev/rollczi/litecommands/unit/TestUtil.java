package dev.rollczi.litecommands.unit;

import dev.rollczi.litecommands.argument.input.RawInputArguments;
import dev.rollczi.litecommands.invocation.Invocation;

import java.util.Arrays;

public final class TestUtil {

    public static Invocation<TestSender> invocation(String command, String... args) {
        return new Invocation<>(new TestSender(), new TestPlatformSender(), command, command, new RawInputArguments(Arrays.asList(args)));
    }

}
