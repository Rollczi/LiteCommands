package dev.rollczi.litecommands.command.permission;

import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.route.Route;
import dev.rollczi.litecommands.test.TestFactory;
import dev.rollczi.litecommands.test.TestPlatform;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ChildRoutePermissionHandleTest {

    TestPlatform platform = TestFactory.withCommands(
        MainCommand.class
    );

    @Route(name = "main")
    static class MainCommand {
        @Execute(required = 0)
        void execute() {}

        @Execute(route = "info", required = 1)
        @Permission("main.info")
        void info() {}

        @Execute(route = "test", required = 1)
        @Permission("main.test")
        void test(@Arg String arg) {}

        @Execute(route = "test", required = 2)
        @Permission("main.test.2")
        void test(@Arg String arg, @Arg String arg2) {}
    }

    @Test
    void test() {
        RequiredPermissions permissions = platform.execute("main", "info")
            .assertRequiredPermissions();

        assertEquals(1, permissions.getPermissions().size());
        assertEquals("main.info", permissions.getPermissions().get(0));

        permissions = platform.execute("main", "test", "value")
            .assertRequiredPermissions();

        assertEquals(1, permissions.getPermissions().size());
        assertEquals("main.test", permissions.getPermissions().get(0));

        permissions = platform.execute("main", "test", "value", "value2")
            .assertRequiredPermissions();

        assertEquals(1, permissions.getPermissions().size());
        assertEquals("main.test.2", permissions.getPermissions().get(0));
    }

}
