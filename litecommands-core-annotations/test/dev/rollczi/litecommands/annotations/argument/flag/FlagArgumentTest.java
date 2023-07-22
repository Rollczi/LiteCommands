package dev.rollczi.litecommands.annotations.argument.flag;

import dev.rollczi.litecommands.annotations.LiteTestSpec;
import dev.rollczi.litecommands.annotations.argument.arg.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.invalid.InvalidUsage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FlagArgumentTest extends LiteTestSpec {

    @Command(name = "test")
    static class TestCommand {

        @Execute
        String test(@Flag("-a") boolean flagA, @Arg String test) {
            return flagA + " " + test;
        }

        @Execute(name = "last")
        String test(@Arg String test, @Flag("-a") boolean flagA) {
            return flagA + " " + test;
        }


    }

    @Test
    @DisplayName("Should parse enabled flag argument")
    void testParseEnabledFlagArgument() {
        platform.execute("test -a value")
            .assertSuccess("true value");
    }

    @Test
    @DisplayName("Should parse disabled flag argument")
    void testParseDisabledFlagArgument() {
        platform.execute("test value")
            .assertSuccess("false value");
    }

    @Test
    @DisplayName("Should not parse invalid flag argument")
    void testParseInvalidFlagArgument() {
        platform.execute("test -b value")
            .assertFailure(InvalidUsage.Cause.TOO_MANY_ARGUMENTS);
    }

    @Test
    @DisplayName("Should suggest flag argument")
    void testSuggestFlagArgument() {
        platform.suggest("test ")
            .assertSuggest("-a", "text", "last");
    }

    @Test
    @DisplayName("Should parse flag argument when it is last and has value")
    void testParseFlagArgumentWhenItIsLastAndHasValue() {
        platform.execute("test last value -a")
            .assertSuccess("true value");
    }

    @Test
    @DisplayName("Should parse disabled flag argument when it is last")
    void testParseFlagArgumentWhenItIsLast() {
        platform.execute("test last -a")
            .assertSuccess("false -a");
    }



}