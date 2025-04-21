package dev.rollczi.litecommands.annotations.root;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.RootCommand;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import dev.rollczi.litecommands.unit.Parsers;
import dev.rollczi.litecommands.unit.TestSender;
import dev.rollczi.litecommands.unit.annotations.LiteTestSpec;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class RootMixedPermissionsTest extends LiteTestSpec {

    static LiteTestConfig config = builder -> builder
        .argumentParser(TestSender.class, Parsers.of(new TestSender()));

    @RootCommand
    public static class TestCommand {
        @Execute(name = "test")
        @Permission("crazytestpermission")
        void executeTest(@Context TestSender player, @Arg("testPlayer") TestSender testPlayer) {
        }

        @Execute(name = "test")
        @Permission("crazytestpermission")
        void executeTest(@Context TestSender player, @Arg("message") String message) {
        }
    }

    @RootCommand
    public static class Test2Command {
        @Execute(name = "test2")
        @Permission("crazytestpermission")
        void executeTest(@Context TestSender player, @Arg("testPlayer") TestSender testPlayer, @Arg("player") Optional<TestSender> target) {
        }

        @Execute(name = "test2")
        @Permission("crazytestpermission")
        void executeTest(@Context TestSender player, @Arg("message") String message) {}
    }

    @Test
    void test() {
        platform.execute("test")
            .assertMissingPermission("crazytestpermission");
    }

    @Test
    void test2() {
        platform.execute("test2")
            .assertMissingPermission("crazytestpermission");
    }

}
