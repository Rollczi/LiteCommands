package dev.rollczi.litecommands.programmatic;

import dev.rollczi.litecommands.command.executor.LiteContext;
import dev.rollczi.litecommands.platform.PlatformSender;
import dev.rollczi.litecommands.unit.LiteCommandsTestFactory;
import dev.rollczi.litecommands.unit.TestPlatform;
import dev.rollczi.litecommands.unit.TestPlatformSender;
import dev.rollczi.litecommands.unit.TestSender;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ResultHandlerTest {

    TestPlatform testPlatform = LiteCommandsTestFactory.startPlatform(builder -> builder.commands(
        new LiteCommand<TestSender>("ban")
            .argument("player", String.class)
            .argumentNullable("reason", String.class)
            .onExecute(context -> {
                String player = context.argument("player", String.class);
                String reason = context.argumentNullable("reason", String.class);

                return "banned " + player + " " + reason;
            }),

        new LiteCommand<TestSender>("ban-object") {

            {
                this.argument("player", String.class);
                this.argumentNullable("reason", String.class);
            }

            @Override
            protected void execute(LiteContext<TestSender> context) {
                String player = context.argument("player", String.class);
                String reason = context.argumentNullable("reason", String.class);

                context.returnResult("banned " + player + " " + reason);
            }
        }
    ));

    @Test
    @DisplayName("Should handle result")
    public void testSuccess() {
        PlatformSender permitted = TestPlatformSender.permitted();

        testPlatform.execute(permitted, "ban test reason")
            .assertSuccess()
            .assertMessage("banned test reason");

        testPlatform.execute(permitted, "ban-object test reason")
            .assertSuccess()
            .assertMessage("banned test reason");
    }


}
