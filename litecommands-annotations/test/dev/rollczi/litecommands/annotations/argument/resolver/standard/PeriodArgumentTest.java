package dev.rollczi.litecommands.annotations.argument.resolver.standard;

import dev.rollczi.litecommands.annotations.LiteTestSpec;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import org.junit.jupiter.api.Test;

import java.time.Period;

class PeriodArgumentTest extends LiteTestSpec {

    @Command(name = "test")
    static class TestCommand {

        @Execute
        Period test(@Arg Period duration) {
            return duration;
        }
    }

    @Test
    void test() {
        platform.execute("test 1d")
            .assertSuccess(Period.ofDays(1));
    }

    @Test
    void testInvalid() {
        platform.execute("test 1acab")
            .assertFailure();
    }

    @Test
    void testMultipleValues() {
        platform.execute("test 7y3w")
            .assertSuccess();
    }

    @Test
    void testReturn() {
        platform.execute("test 7y4mo3d")
            .assertSuccess(Period.ofYears(7).plus(Period.ofMonths(4)).plus(Period.ofDays(3)));
    }
}