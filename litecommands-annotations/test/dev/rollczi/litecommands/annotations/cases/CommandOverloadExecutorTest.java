package dev.rollczi.litecommands.annotations.cases;

import dev.rollczi.litecommands.LiteCommandsFactory;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.unit.TestPlatform;
import dev.rollczi.litecommands.unit.TestSender;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class CommandOverloadExecutorTest {

    @Command(name = "deathrun", aliases = { "dr" })
    static class CommandDeathRun {

        @Execute
        public void noArguments() {}

        @Execute(name = "setup")
        void noArgumentsSetup() {}

        @Execute(name = "setup add")
        void add() {}

        @Execute(name = "setup add")
        void add(@Arg String name) {}

        @Execute(name = "setup add")
        void add(@Arg String name, @Arg String location) {}

        @Execute(name = "setup add")
        void add(@Arg String name, @Arg String location, @Arg String world) {}

        @Execute(name = "setup add")
        void add(@Arg String name, @Arg String location, @Arg String world, @Arg String yaw) {}

        @Execute(name = "setup add")
        void add(@Arg String name, @Arg String location, @Arg String world, @Arg String yaw, @Arg String pitch) {}

        @Execute(name = "setup add")
        void add(@Arg String name, @Arg String location, @Arg String world, @Arg String yaw, @Arg String pitch, @Arg String radius) {}

        @Execute(name = "setup add")
        void add(@Arg String name, @Arg String location, @Arg String world, @Arg String yaw, @Arg String pitch, @Arg String radius, @Arg String height) {}

        @Execute(name = "setup add")
        void add(@Arg String name, @Arg String location, @Arg String world, @Arg String yaw, @Arg String pitch, @Arg String radius, @Arg String height, @Arg String material) {}

    }

    @Test
    @DisplayName("Should create 9 executors for command 'deathrun setup add'")
    void test() {
        TestPlatform testPlatform = new TestPlatform();
        LiteCommandsFactory.builder(TestSender.class, testPlatform)
            .commands(new CommandDeathRun())
            .build();

        CommandRoute<TestSender> command = testPlatform.findCommand("deathrun");
        CommandRoute<TestSender> route = command.getChild("setup").get();
        CommandRoute<TestSender> add = route.getChild("add").get();
        assertThat(add.getExecutors()).hasSize(9);
    }

}
