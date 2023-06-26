package dev.rollczi.litecommands.annotations.invalid;

import dev.rollczi.litecommands.annotations.LiteConfig;
import dev.rollczi.litecommands.annotations.LiteTestSpec;
import dev.rollczi.litecommands.annotations.argument.arg.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.invalid.InvalidUsage;
import dev.rollczi.litecommands.unit.TestSender;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class InvalidHandlerTest extends LiteTestSpec {

    static AtomicReference<InvalidUsage<TestSender>> invalidUsage = new AtomicReference<>();

    static LiteConfig config = builder -> builder
        .invalidUsage((invocation, result) -> invalidUsage.set(result));

    @Command(name = "test")
    static class TestCommand {

        @Execute
        void execute(@Arg("arg") int test) {}

    }

    @Test
    void test() {
        platform.execute("test a")
            .assertFailedAs(InvalidUsage.Cause.class);

        InvalidUsage<TestSender> usage = invalidUsage.get();

        assertNotNull(usage);
        assertEquals(InvalidUsage.Cause.INVALID_ARGUMENT, usage.getCause());
        assertEquals("/test <arg>", usage.getSchematic().first());
    }

}
