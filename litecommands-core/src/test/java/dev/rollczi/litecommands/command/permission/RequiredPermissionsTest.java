package dev.rollczi.litecommands.command.permission;

import dev.rollczi.litecommands.test.TestFactory;
import dev.rollczi.litecommands.test.TestHandle;
import dev.rollczi.litecommands.test.TestPlatform;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.section.CommandSection;
import dev.rollczi.litecommands.command.route.Route;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RequiredPermissionsTest {

    TestPlatform platform = TestFactory.withCommands(PermissionsCommand.class);

    @Test
    void testRequiredPermissionsOfMethod() {
        CommandSection<TestHandle> section = platform.getSection("test");

        assertNotNull(section);
        assertTrue(section.meta().getPermissions().contains("dev.rollczi.litecommands"));

        RequiredPermissions permissions = RequiredPermissions.of(section.meta(), platform.createSender());

        assertFalse(permissions.isEmpty());
        assertEquals(1, permissions.getPermissions().size());
        assertTrue(permissions.getPermissions().contains("dev.rollczi.litecommands"));
    }

    @Test
    void executeCommandTest() {
        RequiredPermissions permissions = platform.execute("test")
                .assertInvalid()
                .assertResultIs(RequiredPermissions.class);

        List<String> perm = permissions.getPermissions();

        assertEquals(2, perm.size());
        assertEquals("dev.rollczi.litecommands", perm.get(0));
        assertEquals("dev.rollczi.litecommands.execute", perm.get(1));
    }

    @Test
    void executeCommandSiema() {
        RequiredPermissions permissions = platform.execute("test", "siema")
                .assertInvalid()
                .assertResultIs(RequiredPermissions.class);

        List<String> perm = permissions.getPermissions();

        assertEquals(2, perm.size());
        assertEquals("dev.rollczi.litecommands", perm.get(0));
        assertEquals("dev.rollczi.litecommands.execute.siema", perm.get(1));
    }

    @Route(name = "test")
    @Permission("dev.rollczi.litecommands")
    static class PermissionsCommand {

        @Execute
        @Permission("dev.rollczi.litecommands.execute")
        void execute() {}

        @Execute(route = "siema")
        @Permission("dev.rollczi.litecommands.execute.siema")
        void executeSub() {}
    }

}