package dev.rollczi.litecommands.programmatic;

import dev.rollczi.litecommands.unit.LiteCommandsTestFactory;
import dev.rollczi.litecommands.unit.TestPlatform;
import dev.rollczi.litecommands.unit.TestSender;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OptionalArgTest {

    TestPlatform testPlatform = LiteCommandsTestFactory.startPlatform(builder -> builder.commands(
        new LiteCommand<TestSender>("ban")
            .argument("player", String.class)
            .argumentOptional("reason", String.class)
            .execute(context -> {
                TestSender sender = context.invocation().sender();
                String player = context.argument("player", String.class);
                String reason = context.argumentOptional("reason", String.class)
                    .orElse("none");

                sender.sendMessage("banned " + player + " " + reason);
            })
    ));

    @Test
    @DisplayName("Should execute command without argument")
    public void testSuccessOptional() {
        testPlatform.execute("ban test")
            .assertSuccess()
            .assertMessage("banned test none");
    }

    @Test
    @DisplayName("Should execute command with argument")
    public void testSuccess() {
        testPlatform.execute("ban test reason")
            .assertSuccess()
            .assertMessage("banned test reason");
    }

}
