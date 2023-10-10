package dev.rollczi.litecommands.annotations.argument.resolver.standard;

import dev.rollczi.litecommands.annotations.LiteTestSpec;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.join.Join;
import org.junit.jupiter.api.Test;

import java.time.Duration;

class DurationArgumentTest extends LiteTestSpec {

    @Command(name = "test")
    static class TestCommand {

        @Execute
        void test(@Arg Duration duration) {
        }

    }

    @Test
    void test() {
        platform.execute("test 1d")
            .assertSuccess();
    }

    @Test
    void testInvalid() {
        platform.execute("test 1abcd")
            .assertFailure();
    }

    @Test
    void testMultipleValues() {
        platform.execute("test 1y7d1h1m1s1ms1us1ns")
            .assertSuccess();
    }

}