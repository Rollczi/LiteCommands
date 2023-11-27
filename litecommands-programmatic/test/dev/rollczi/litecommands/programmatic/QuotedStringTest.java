package dev.rollczi.litecommands.programmatic;

import dev.rollczi.litecommands.unit.LiteCommandsTestFactory;
import dev.rollczi.litecommands.unit.TestPlatform;
import dev.rollczi.litecommands.unit.TestSender;
import org.junit.jupiter.api.Test;

public class QuotedStringTest {

    TestPlatform platform = LiteCommandsTestFactory.startPlatform(builder -> builder.commands(
        new LiteCommand<TestSender>("test")
            .argument("before", String.class)
            .argumentQuoted("message")
            .argument("after", String.class)
            .onExecute(context -> {
                TestSender sender = context.invocation().sender();

                String before = context.argument("before", String.class);
                String message = context.argumentQuoted("message");
                String after = context.argument("after", String.class);

                sender.sendMessage(before + "-" + message + "-" + after);
            })
    ));

    @Test
    void testSingle() {
        platform.execute("test before \"test\" after")
            .assertMessage("before-test-after");
    }

    @Test
    void testMultiple() {
        platform.execute("test before \"siema co tam\" after")
            .assertMessage("before-siema co tam-after");
    }

    @Test
    void testSuggestionsEmpty() {
        platform.suggest("test before ")
            .assertSuggest("\"\"", "\"<message>\"");
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

}
