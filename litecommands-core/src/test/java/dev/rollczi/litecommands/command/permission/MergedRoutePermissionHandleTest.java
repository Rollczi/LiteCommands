package dev.rollczi.litecommands.command.permission;

import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.route.Route;
import dev.rollczi.litecommands.test.TestFactory;
import dev.rollczi.litecommands.test.TestPlatform;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MergedRoutePermissionHandleTest {

    TestPlatform platform = TestFactory.withCommands(
        MainCommand.class,
        InfoCommand.class,
        WithCommand.class,
        OtherClass.class
    );

    @Route(name = "main")
    static class MainCommand {
        @Execute(required = 0)
        @Permission("main.*")
        void execute() {}
    }

    @Route(name = "main")
    static class InfoCommand {
        @Execute(route = "info", required = 0)
        @Permission("main.info")
        void info() {}
    }

    @Route(name = "main")
    static class WithCommand {
        @Execute(route = "with", required = 1)
        @Permission("main.with")
        void execute(@Arg String text) {}
    }

    @Route(name = "main")
    static class OtherClass {
        @Execute(route = "other", required = 2)
        @Permission("main.other")
        void execute(@Arg String text, @Arg String other) {}
    }


    @Test
    public void test() {
        assertPermission("main.*", "main");
        assertPermission("main.info", "main", "info");
        assertPermission("main.with", "main", "with", "value");
        assertPermission("main.other", "main", "other", "value", "value");
    }

    private void assertPermission(String permission, String command, String... args) {
        RequiredPermissions permissions = platform.execute(command, args).assertResultIs(RequiredPermissions.class);

        assertEquals(permission, permissions.getPermissions().get(0));
    }

}
