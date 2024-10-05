package dev.rollczi.litecommands.programmatic;

import dev.rollczi.litecommands.platform.PlatformSender;
import dev.rollczi.litecommands.unit.LiteCommandsTestFactory;
import dev.rollczi.litecommands.unit.TestPlatform;
import dev.rollczi.litecommands.unit.TestPlatformSender;
import dev.rollczi.litecommands.unit.TestSender;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class NullableArgTest {

    TestPlatform testPlatform = LiteCommandsTestFactory.startPlatform(builder -> builder.commands(
        new LiteCommand<TestSender>("ban")
            .argument("player", String.class)
            .argumentNullable("reason", String.class)
            .execute(context -> {
                TestSender sender = context.invocation().sender();
                String player = context.argument("player", String.class);
                String reason = context.argumentNullable("reason", String.class);

                sender.sendMessage("banned " + player + " " + reason);
            })
    ));

    @Test
    @DisplayName("Should success when all requirements are met")
    public void testSuccess() {
        PlatformSender permitted = TestPlatformSender.permitted();

        testPlatform.execute(permitted, "ban test")
            .assertSuccess()
            .assertMessage("banned test null");

        testPlatform.execute(permitted, "ban test reason")
            .assertSuccess()
            .assertMessage("banned test reason");
    }

}
