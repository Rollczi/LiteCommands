package dev.rollczi.litecommands.programmatic;

import dev.rollczi.litecommands.invalidusage.InvalidUsage;
import dev.rollczi.litecommands.message.LiteMessages;
import dev.rollczi.litecommands.platform.PlatformSender;
import dev.rollczi.litecommands.unit.LiteCommandsTestFactory;
import dev.rollczi.litecommands.unit.TestPlatform;
import dev.rollczi.litecommands.unit.TestPlatformSender;
import dev.rollczi.litecommands.unit.TestSender;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static dev.rollczi.litecommands.permission.MissingPermissions.missing;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ProgrammaticApiTest {

    String PERMISSION = "example.ban";

    TestPlatform testPlatform = LiteCommandsTestFactory.startPlatform(builder -> builder.commands(LiteCommandsProgrammatic.of(
        new LiteCommand<TestSender>("ban")
            .permissions(PERMISSION)
            .argument("player", String.class)
            .onExecute(context -> {
                TestSender sender = context.invocation().sender();
                sender.sendMessage("banned");
            })
    )));

    @Test
    @DisplayName("Should success when all requirements are met")
    public void testSuccess() {
        PlatformSender permitted = TestPlatformSender.permitted(PERMISSION);

        testPlatform.execute(permitted, "ban test")
            .assertSuccess()
            .assertMessage("banned");
    }

    @Test
    @DisplayName("Should fail when missing permissions")
    public void testMissingPermissions() {
        String expectedMessage = LiteMessages.MISSING_PERMISSIONS.getDefaultMessage(missing(PERMISSION)).toString();

        testPlatform.execute("ban test")
            .assertMissingPermission(PERMISSION)
            .assertMessage(expectedMessage);
    }

    @Test
    @DisplayName("Should fail when missing argument")
    public void testMissingArgument() {
        PlatformSender permitted = TestPlatformSender.permitted(PERMISSION);

        InvalidUsage failed = testPlatform.execute(permitted, "ban")
            .assertFailedAs(InvalidUsage.class);

        assertThat(failed.getCause()).isEqualTo(InvalidUsage.Cause.MISSING_ARGUMENT);
    }

}
