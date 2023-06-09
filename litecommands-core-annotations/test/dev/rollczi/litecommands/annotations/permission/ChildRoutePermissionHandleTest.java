package dev.rollczi.litecommands.annotations.permission;

import dev.rollczi.litecommands.annotations.LiteTestSpec;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.route.Route;
import dev.rollczi.litecommands.permission.MissingPermissions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ChildRoutePermissionHandleTest extends LiteTestSpec {

    @Route(name = "main")
    static class MainCommand {
        @Execute
        void execute() {}

        @Execute(route = "info")
        @Permission("main.info")
        void info() {}

        @Execute(route = "test")
        @Permission("main.test")
        void test(@Arg String arg) {}

        @Execute(route = "test")
        @Permission("main.test.2")
        void test(@Arg String arg, @Arg String arg2) {}

    }
    @Test
    void testSinglePermission() {
        MissingPermissions permissions = platform.execute("main", "info")
            .assertFailedAs(MissingPermissions.class);

        assertEquals(1, permissions.getPermissions().size());
        assertEquals("main.info", permissions.getPermissions().get(0));
    }

    @Test
    void testPermissionInSameRoute() {
        MissingPermissions permissions = platform.execute("main", "test", "value")
            .assertFailedAs(MissingPermissions.class);

        assertEquals(1, permissions.getPermissions().size());
        assertEquals("main.test", permissions.getPermissions().get(0));

        permissions = platform.execute("main", "test", "value", "value2")
            .assertFailedAs(MissingPermissions.class);

        assertEquals(1, permissions.getPermissions().size());
        assertEquals("main.test.2", permissions.getPermissions().get(0));
    }

}
