package dev.rollczi.litecommands.annotations.strict;

import dev.rollczi.litecommands.unit.annotations.LiteTestSpec;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.invalidusage.InvalidUsage;
import dev.rollczi.litecommands.strict.StrictMode;
import org.junit.jupiter.api.Test;

class StrictTest extends LiteTestSpec {

    @Command(name = "test", strict = StrictMode.DISABLED)
    static class TestCommand {

        @Execute(name = "no-strict")
        void noStrict(@Arg String arg) {}


        @Execute(name = "strict", strict = StrictMode.ENABLED)
        void strict(@Arg String arg) {}

    }

    @Test
    void invalid() {
        platform.execute("test")
            .assertFailureInvalid(InvalidUsage.Cause.UNKNOWN_COMMAND);
    }

    @Test
    void noStrict() {
        platform.execute("test no-strict")
            .assertFailureInvalid(InvalidUsage.Cause.MISSING_ARGUMENT);

        platform.execute("test no-strict arg")
            .assertSuccess();

        platform.execute("test no-strict arg arg")
            .assertSuccess();
    }

    @Test
    void strict() {
        platform.execute("test strict")
            .assertFailureInvalid(InvalidUsage.Cause.MISSING_ARGUMENT);

        platform.execute("test strict arg")
            .assertSuccess();

        platform.execute("test strict arg arg")
            .assertFailureInvalid(InvalidUsage.Cause.TOO_MANY_ARGUMENTS);
    }

}
