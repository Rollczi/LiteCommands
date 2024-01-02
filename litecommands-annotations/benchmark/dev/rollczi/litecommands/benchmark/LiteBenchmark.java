package dev.rollczi.litecommands.benchmark;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.unit.LiteCommandsTestFactory;
import dev.rollczi.litecommands.unit.TestPlatform;
import java.time.Duration;
import java.time.Instant;
import org.openjdk.jmh.runner.RunnerException;

import java.util.Optional;

public class LiteBenchmark {

    public static final int ITERATIONS = 1_000_000;
    static TestPlatform testPlatform;

    @Command(name = "test")
    static class TestCommand {
        @Execute
        void number(@Arg String first, @Arg String second) {}

        @Execute(name = "number")
        void execute(@Arg int first, @Arg double second) {}

        @Execute(name = "sub")
        void execute(@Arg String first, @Arg Optional<String> second) {}

        @Execute(name = "duration")
        void execute(@Arg Duration duration, @Arg String second) {}
    }

    public static void main(String[] args) throws RunnerException {
        testPlatform = LiteCommandsTestFactory.startPlatform(builder -> builder
            .commands(
                new TestCommand()
            )
        );

        for (int count = 0; count < 3; count++) {
            System.out.println("Iteration: " + count);
            Stopper stopper = new Stopper("string");
            for (int i = 0; i < ITERATIONS; i++) {
                testPlatform.execute("test first second");
            }
            stopper.stop();

            stopper = new Stopper("number");
            for (int i = 0; i < ITERATIONS; i++) {
                testPlatform.execute("test number 1 2");
            }
            stopper.stop();

            stopper = new Stopper("sub");
            for (int i = 0; i < ITERATIONS; i++) {
                testPlatform.execute("test sub first");
            }
            stopper.stop();

            stopper = new Stopper("sub optional");
            for (int i = 0; i < ITERATIONS; i++) {
                testPlatform.execute("test sub first second");
            }
            stopper.stop();

            stopper = new Stopper("duration");
            for (int i = 0; i < ITERATIONS; i++) {
                testPlatform.execute("test duration 1m1s second");
            }
            stopper.stop();
        }
    }


    public static class Stopper {
        private final String name;
        private final long start;

        public Stopper(String name) {
            this.name = name;
            this.start = System.nanoTime();
        }

        public void stop() {
            long end = System.nanoTime();
            System.out.println("Time: " + (end - start) / 1000000.0 + "ms, name: " + name);
        }
    }

}
