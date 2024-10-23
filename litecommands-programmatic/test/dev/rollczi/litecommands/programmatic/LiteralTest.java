package dev.rollczi.litecommands.programmatic;

import dev.rollczi.litecommands.unit.LiteCommandsTestFactory;
import dev.rollczi.litecommands.unit.TestPlatform;
import dev.rollczi.litecommands.unit.TestSender;
import org.junit.jupiter.api.Test;

class LiteralTest {

    TestPlatform platform = LiteCommandsTestFactory.startPlatform(builder -> builder.commands(
        new LiteCommand<TestSender>("user")
            .argument("name", String.class)
            .literal("group set")
            .argument("group", String.class)
            .execute(context -> {
                TestSender sender = context.invocation().sender();
                String name = context.argument("name", String.class);
                String group = context.argument("group", String.class);

                sender.sendMessage(name + " group set " + group);
            })
    ));

    @Test
    public void testLiteralExecute() {
        platform.execute("user Rollczi group set Admin")
            .assertMessage("Rollczi group set Admin");
    }

    @Test
    public void testLiteralSuggest() {
        platform.suggest("user Rollczi ")
            .assertSuggest("group set");

        platform.suggest("user Rollczi group set ")
            .assertSuggest("<group>");
    }

}
