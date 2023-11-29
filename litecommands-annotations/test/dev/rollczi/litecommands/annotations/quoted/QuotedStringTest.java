package dev.rollczi.litecommands.annotations.quoted;

import dev.rollczi.litecommands.annotations.LiteTestSpec;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import org.junit.jupiter.api.Test;

class QuotedStringTest extends LiteTestSpec {

    @Command(name = "test")
    static class TestCommand {

        @Execute
        String test(@Arg String before, @Arg("message") @Quoted String message, @Arg("after") String after) {
            return before + "-" + message + "-" + after;
        }

        @Execute(name = "end-quoted")
        String test2(@Arg String before, @Arg("message") @Quoted String message) {
            return before + "-" + message;
        }

    }

    @Test
    void testSingle() {
        platform.execute("test before \"test\" after")
            .assertSuccess("before-test-after");
    }

    @Test
    void testNoQuoted() {
        platform.execute("test before test after")
            .assertSuccess("before-test-after");
        platform.execute("test end-quoted before \"test after end")
            .assertSuccess("before-test after end");
    }

    @Test
    void testMultiple() {
        platform.execute("test before \"siema co tam\" after")
            .assertSuccess("before-siema co tam-after");
    }

    @Test
    void testSuggestionsEmpty() {
        platform.suggest("test before ")
            .assertSuggest("\"");
    }

    @Test
    void testSuggestions() {
        platform.suggest("test before \"")
            .assertSuggest("\"\"", "\"<message>\"");

        platform.suggest("test before \"<")
            .assertSuggest("\"<\"", "\"<message>\"");

        platform.suggest("test before \"<message>")
            .assertSuggest("\"<message>\"");
    }

    @Test
    void testSuggestionsEmptyInvalid() {
        platform.suggest("test before \" ")
            .assertSuggest("\"");
    }

    @Test
    void testSuggestionsAfter() {
        platform.suggest("test before \"<message>\" ")
            .assertSuggest("<after>");
        platform.suggest("test before \" \" ")
            .assertSuggest("<after>");
    }

}
