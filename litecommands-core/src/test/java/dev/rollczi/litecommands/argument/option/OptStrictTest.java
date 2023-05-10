package dev.rollczi.litecommands.argument.option;

import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.route.Route;
import dev.rollczi.litecommands.test.TestFactory;
import dev.rollczi.litecommands.test.TestPlatform;
import org.junit.jupiter.api.Test;
import panda.std.Option;

class OptStrictTest {

    TestPlatform platform = TestFactory.withCommands(Command.class);

    @Route(name = "command")
    static class Command {

        @Execute(min = 0, max = 1)
        public String execute(@Opt(strict = true) Option<Integer> x) {
            return "first";
        }


        @Execute(min = 0, max = 2)
        public String execute(@Opt(strict = true) Option<Integer> x, @Opt(strict = true) Option<Integer> y) {
            return "second";
        }

    }

    @Test
    void testSingleOptStrict() {
        platform.execute("command", "1")
            .assertSuccess()
            .assertResult("first");

    }

    @Test
    void testSingleOptStrictFail() {
        platform.execute("command", "text")
            .assertInvalid();
    }

    @Test
    void testDoubleOptStrict() {
        platform.execute("command", "1", "2")
            .assertSuccess()
            .assertResult("second");
    }
}
