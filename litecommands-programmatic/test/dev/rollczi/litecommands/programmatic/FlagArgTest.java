package dev.rollczi.litecommands.programmatic;

import dev.rollczi.litecommands.unit.LiteCommandsTestFactory;
import dev.rollczi.litecommands.unit.TestPlatform;
import dev.rollczi.litecommands.unit.TestSender;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FlagArgTest {

    TestPlatform testPlatform = LiteCommandsTestFactory.startPlatform(builder -> builder.commands(
        new LiteCommand<TestSender>("ban")
            .argument("player", String.class)
            .argumentFlag("--s")
            .argumentJoin("reason")
            .execute(context -> {
                TestSender sender = context.invocation().sender();
                String player = context.argument("player", String.class);
                String reason = context.argumentJoin("reason");
                boolean isSilent = context.argumentFlag("--s");

                sender.sendMessage("banned " + player + " " + reason + " " + (isSilent ? "silent" : "none"));
            })
    ));

    @Test
    @DisplayName("Should execute command without flag")
    public void testSuccessOptional() {
        testPlatform.execute( "ban test reason of the ban")
            .assertSuccess()
            .assertMessage("banned test reason of the ban none");
    }

    @Test
    @DisplayName("Should execute command with argument")
    public void testSuccess() {
        testPlatform.execute("ban test --s reason of the ban")
            .assertSuccess()
            .assertMessage("banned test reason of the ban silent");
    }

    @Test
    @DisplayName("Should suggest flag and next argument, but only when next argument is while providing")
    public void testSuggest() {
        testPlatform.suggest("ban test ")
            .assertSuggest("--s", "<reason>");

        testPlatform.suggest("ban test reason of -")
            .assertSuggest("-");
    }

}
