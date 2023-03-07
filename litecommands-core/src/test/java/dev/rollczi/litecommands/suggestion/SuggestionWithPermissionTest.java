package dev.rollczi.litecommands.suggestion;

import dev.rollczi.litecommands.command.permission.Permission;
import dev.rollczi.litecommands.command.route.Route;
import dev.rollczi.litecommands.test.TestFactory;
import dev.rollczi.litecommands.test.TestPermissionSender;
import dev.rollczi.litecommands.test.TestPlatform;
import org.junit.jupiter.api.Test;

public class SuggestionWithPermissionTest {

    TestPlatform platform = TestFactory.withCommands(Command.class);

    @Route(name = "test")
    @Permission("permission.test")
    static class Command {

        @Route(name = "a")
        @Permission("test.a")
        void a() {}

        @Route(name = "b")
        @Permission("test.b")
        void b() {}
    }

    @Test
    void testWithOutPermission() {
        platform.suggest(TestPermissionSender.without(), "test", "")
            .assertWith();
    }

    @Test
    void testWithPermission() {
        platform.suggest(TestPermissionSender.with("permission.test"), "test", "")
            .assertWith();

        platform.suggest(TestPermissionSender.with("permission.test", "test.a"), "test", "")
            .assertWith("a");

        platform.suggest(TestPermissionSender.with("permission.test", "test.b"), "test", "")
            .assertWith("b");

        platform.suggest(TestPermissionSender.with("permission.test", "test.a", "test.b"), "test", "")
            .assertWith("a", "b");
    }


}
