package dev.rollczi.litecommands.annotations.argument.flag;

import dev.rollczi.litecommands.annotations.LiteTestSpec;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.invalidusage.InvalidUsage;
import dev.rollczi.litecommands.annotations.flag.Flag;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class FlagArgumentTest extends LiteTestSpec {

    @Command(name = "test")
    static class TestCommand {

        @Execute
        String test(@Flag("-a") boolean flagA, @Arg("test") String test) {
            return flagA + " " + test;
        }

        @Execute(name = "last")
        String test(@Arg("test") String test, @Flag("-a") boolean flagA) {
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
        InvalidUsage invalidUsage = platform.execute("test -b value")
            .assertFailedAs(InvalidUsage.class);

        assertThat(invalidUsage.getCause())
            .isEqualByComparingTo(InvalidUsage.Cause.TOO_MANY_ARGUMENTS);
    }

    @Test
    @DisplayName("Should suggest flag argument")
    void testSuggestFlagArgument() {
        platform.suggest("test ")
            .assertSuggest("-a", "<test>", "last");
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
