package dev.rollczi.litecommands.command.permission;

import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.command.execute.ExecuteResult;
import dev.rollczi.litecommands.command.section.CommandSection;
import dev.rollczi.litecommands.command.section.Section;
import dev.rollczi.litecommands.implementation.LiteFactory;
import dev.rollczi.litecommands.implementation.TestHandle;
import dev.rollczi.litecommands.implementation.TestPlatform;
import dev.rollczi.litecommands.platform.LiteSender;
import org.junit.jupiter.api.Test;
import panda.std.Result;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LitePermissionsTest {

    private final TestPlatform testPlatform = new TestPlatform();
    private final LiteCommands<TestHandle> liteCommands = LiteFactory.builder(TestHandle.class)
            .platform(testPlatform)
            .resultHandler(LitePermissions.class, (permissions, sender, liteInvocation) -> {})
            .argument(String.class, (invocation, argument) -> Result.ok(argument))
            .command(PermissionsCommand.class)
            .register();

    @Test
    void testLitePermissionsOfMethod() {
        CommandSection section = liteCommands.getCommandService().getSection("test");

        assertNotNull(section);
        assertTrue(section.permissions().contains("dev.rollczi.litecommands"));

        LitePermissions permissions = LitePermissions.of(section, new PermissionSenderTest());

        assertFalse(permissions.isEmpty());
        assertEquals(1, permissions.getPermissions().size());
        assertTrue(permissions.getPermissions().contains("dev.rollczi.litecommands"));
    }

    @Test
    void executeCommandTest() {
        ExecuteResult result = testPlatform.execute("test");

        assertTrue(result.isInvalid());
        assertInstanceOf(LitePermissions.class, result.getResult());
        assertTrue(((LitePermissions) result.getResult()).getPermissions().contains("dev.rollczi.litecommands"));
    }

    @Section(route = "test")
    @Permission("dev.rollczi.litecommands")
    static class PermissionsCommand {

    }

    static class PermissionSenderTest implements LiteSender {

        @Override
        public boolean hasPermission(String permission) {
            return false;
        }

        @Override
        public Object getHandle() {
            return null;
        }

    }

}