package dev.rollczi.litecommands.annotations.argument.resolver.standard;

import dev.rollczi.litecommands.annotations.LiteTestSpec;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import org.junit.jupiter.api.Test;

class BooleanArgumentTest extends LiteTestSpec {

    @Command(name = "test")
    static class TestCommand {

        @Execute(name = "primitive")
        boolean test(@Arg boolean value) {
            return value;
        }

        @Execute(name = "object")
        Boolean test(@Arg Boolean value) {
            return value;
        }
    }

    @Test
    void testPrimitive() {
        platform.execute("test primitive true")
            .assertSuccess(true);

        platform.execute("test primitive false")
            .assertSuccess(false);
    }

    @Test
    void testObject() {
        platform.execute("test object true")
            .assertSuccess(true);

        platform.execute("test object false")
            .assertSuccess(false);
    }

    @Test
    void testInvalid() {
        platform.execute("test primitive invalid")
            .assertFailure();

        platform.execute("test object invalid")
            .assertFailure();
    }

    @Test
    void testSuggestions() {
        platform.suggest("test primitive ")
            .assertSuggest("true", "false");

        platform.suggest("test primitive t")
            .assertSuggest("true");

        platform.suggest("test primitive f")
            .assertSuggest("false");

        platform.suggest("test object ")
            .assertSuggest("true", "false");

        platform.suggest("test object t")
            .assertSuggest("true");

        platform.suggest("test object f")
            .assertSuggest("false");
    }

}