package dev.rollczi.litecommands.suggestion;

import dev.rollczi.litecommands.argument.Arg;
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

        enum TestEnum { A, B }

        @Route(name = "a")
        @Permission("test.a")
        void a() {}

        @Route(name = "a")
        @Permission("test.a.withArg")
        void a(@Arg TestEnum testEnum) {}

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

    @Test
    void testWithExecutor() {
        platform.suggest(TestPermissionSender.with("permission.test", "test.a.withArg"), "test", "a", "")
            .assertWith("A", "B");

        platform.suggest(TestPermissionSender.with("permission.test", "test.a"), "test", "a", "")
            .assertWith();
    }


}
