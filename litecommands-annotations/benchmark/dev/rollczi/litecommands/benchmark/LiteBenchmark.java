package dev.rollczi.litecommands.benchmark;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.unit.LiteCommandsTestFactory;
import dev.rollczi.litecommands.unit.TestPlatform;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;

import java.util.Optional;

@State(Scope.Benchmark)
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
