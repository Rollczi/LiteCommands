package dev.rollczi.litecommands.annotations.argument.resolver.standard;

import dev.rollczi.litecommands.annotations.LiteTestSpec;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import org.junit.jupiter.api.Test;

import java.time.Duration;

class DurationArgumentTest extends LiteTestSpec {

    @Command(name = "test")
    static class TestCommand {

        @Execute
        Duration test(@Arg Duration duration) {
            return duration;
        }
    }

    @Test
    void test() {
        platform.execute("test 1d")
            .assertSuccess(Duration.ofDays(1));
    }

    @Test
    void testInvalid() {
        platform.execute("test 1acab")
            .assertFailure();
    }

    @Test
    void testMultipleValues() {
        platform.execute("test 7d1h1m1s1ms1us1ns")
            .assertSuccess();
    }

    @Test
    void testReturn() {
        platform.execute("test 1d3m")
            .assertSuccess(Duration.ofDays(1).plus(Duration.ofMinutes(3)));
    }
}