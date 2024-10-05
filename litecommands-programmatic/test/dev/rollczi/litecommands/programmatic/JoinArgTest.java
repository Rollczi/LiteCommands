package dev.rollczi.litecommands.programmatic;

import dev.rollczi.litecommands.invalidusage.InvalidUsage;
import dev.rollczi.litecommands.unit.LiteCommandsTestFactory;
import dev.rollczi.litecommands.unit.TestPlatform;
import dev.rollczi.litecommands.unit.TestSender;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JoinArgTest {

    TestPlatform testPlatform = LiteCommandsTestFactory.startPlatform(builder -> builder.commands(
        new LiteCommand<TestSender>("ban")
            .argument("player", String.class)
            .argumentJoin("reason", "-", 5)
            .execute(context -> {
                TestSender sender = context.invocation().sender();
                String player = context.argument("player", String.class);
                String reason = context.argumentJoin("reason");

                sender.sendMessage("banned " + player + " " + reason);
            })
    ));

    @Test
    @DisplayName("Should execute command with join argument")
    public void testSuccessJoin() {
        testPlatform.execute("ban test reason of the ban")
            .assertSuccess()
            .assertMessage("banned test reason-of-the-ban");

        testPlatform.execute( "ban test reason of the ban !")
            .assertSuccess()
            .assertMessage("banned test reason-of-the-ban-!");
    }

    @Test
    @DisplayName("Should not parse invalid join argument")
    public void testParseInvalidJoinArgument() {
        testPlatform.execute("ban test reason of the ban ! more")
            .assertFailureInvalid(InvalidUsage.Cause.TOO_MANY_ARGUMENTS);
    }

    @Test
    @DisplayName("Should suggest join argument")
    public void testSuggest() {
        testPlatform.suggest("ban test ")
            .assertSuggest("<reason>");

        testPlatform.suggest("ban test reason of the ban ")
            .assertSuggest();
    }

}
