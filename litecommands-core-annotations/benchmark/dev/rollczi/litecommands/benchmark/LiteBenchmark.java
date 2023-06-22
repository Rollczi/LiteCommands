package dev.rollczi.litecommands.benchmark;

import dev.rollczi.litecommands.annotations.LiteAnnotationCommands;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.unit.AssertExecute;
import dev.rollczi.litecommands.unit.LiteCommandsTestFactory;
import dev.rollczi.litecommands.unit.TestPlatform;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Optional;

@State(Scope.Benchmark)
public class LiteBenchmark {

    TestPlatform testPlatform;

    @Setup
    public void setUp() {
        testPlatform = LiteCommandsTestFactory.startPlatform(builder -> builder
            .commands(LiteAnnotationCommands.of(
                new TestCommand()
            ))
        );
    }

    @Command(name = "test")
    static class TestCommand {
        @Execute
        void execute(@Arg String first, @Arg String second) {}
        @Execute(name = "sub")
        void execute(@Arg String first, @Arg Optional<String> second) {}
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    public AssertExecute executeCommand() {
        return testPlatform.execute("test first second");
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    public AssertExecute executeSubCommand() {
        return testPlatform.execute("test sub first second");
    }

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
            .include(LiteBenchmark.class.getSimpleName())
            .build();

        new Runner(options).run();
    }

}
