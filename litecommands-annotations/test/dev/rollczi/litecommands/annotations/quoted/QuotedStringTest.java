package dev.rollczi.litecommands.annotations.quoted;

import dev.rollczi.litecommands.annotations.LiteTestSpec;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.flag.Flag;
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

        @Execute(name = "flag-end")
        String test3(@Arg("message") @Quoted String message, @Flag("-f") boolean flag) {
            return message + "-" + flag;
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
        platform.execute("test flag-end test -f")
            .assertSuccess("test-true");
    }

    @Test
    void testQuotedWithQuote() {
        platform.execute("test before \"test\\\"\" after")
            .assertSuccess("before-test\"-after");

        platform.execute("test before \" \\\"test\\\" \" after")
            .assertSuccess("before- \"test\" -after");
    }

    @Test
    void testMultiple() {
        platform.execute("test before \"siema co tam\" after")
            .assertSuccess("before-siema co tam-after");
    }

    @Test
    void testFlag() {
        platform.execute("test flag-end \"test\" -f")
            .assertSuccess("test-true");
        platform.execute("test flag-end \"test\"")
            .assertSuccess("test-false");
    }

    @Test
    void testSuggestionsEmpty() {
        platform.suggest("test before ")
            .assertSuggest("\"\"", "\"", "<message>", "\"<message>\"");
    }

    @Test
    void testSuggestionsWithoutQuotes() {
        platform.suggest("test before test")
            .assertSuggest("test");
        platform.suggest("test flag-end test")
            .assertSuggest("test");
        platform.suggest("test flag-end test ")
            .assertSuggest("-f");
    }

    @Test
    void testSuggestionsWithQuotesInQuotes() {
        platform.suggest("test flag-end \"test some\\\" ")
            .assertSuggest("\"");
    }


    @Test
    void testSuggestions() {
        platform.suggest("test before \"")
            .assertSuggest("\"\"", "\"", "\"<message>\"");

        platform.suggest("test before \"<")
            .assertSuggest("\"<\"", "\"<", "\"<message>\"");

        platform.suggest("test before \"<message>")
            .assertSuggest("\"<message>\"", "\"<message>");
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
