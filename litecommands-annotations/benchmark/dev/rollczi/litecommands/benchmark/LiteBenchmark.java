package dev.rollczi.litecommands.benchmark;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.unit.LiteCommandsTestFactory;
import dev.rollczi.litecommands.unit.TestPlatform;
import org.openjdk.jmh.runner.RunnerException;

import java.util.Optional;

public class LiteBenchmark {

    static TestPlatform testPlatform;

    @Command(name = "test")
    static class TestCommand {
        @Execute
        void execute(@Arg String first, @Arg String second) {}
        @Execute(name = "sub")
        void execute(@Arg String first, @Arg Optional<String> second) {}
    }

    public static void main(String[] args) throws RunnerException {
        testPlatform = LiteCommandsTestFactory.startPlatform(builder -> builder
            .commands(
                new TestCommand()
            )
        );

        for (int i = 0; i < 1_000_000; i++) {
            testPlatform.execute("test first second");
            testPlatform.execute("test sub first second");
        }
    }

}
