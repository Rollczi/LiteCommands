package dev.rollczi.litecommands.annotations.async;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.programmatic.LiteCommand;
import static dev.rollczi.litecommands.programmatic.LiteProgrammatic.async;
import dev.rollczi.litecommands.scheduler.SchedulerExecutorPoolImpl;
import dev.rollczi.litecommands.unit.AssertExecute;
import dev.rollczi.litecommands.unit.LiteCommandsTestFactory;
import dev.rollczi.litecommands.unit.TestPlatform;
import dev.rollczi.litecommands.unit.TestSender;
import dev.rollczi.litecommands.util.FutureUtil;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AsyncArgumentsInitializeTest {

    private final static List<Class<?>> ARGUMENTS_TYPES = Arrays.asList(
        String.class,
        Integer.class,
        Long.class,
        Double.class,
        Float.class,
        Byte.class,
        Short.class,
        Boolean.class,
        Duration.class,
        Period.class,
        Instant.class,
        LocalDateTime.class
    );

    @Test
    @DisplayName("Test for async arguments parsers initialization, should not throw any exceptions while running")
    void test() {
        LiteCommand<TestSender> command = new LiteCommand<>("test");

        for (Class<?> type : ARGUMENTS_TYPES) {
            command.argument(async(Argument.of(type.getSimpleName(), type)));
        }

        List<CompletableFuture<AssertExecute>> futures = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            TestPlatform platform = LiteCommandsTestFactory.startPlatform(config -> config
                .scheduler(new SchedulerExecutorPoolImpl("lite-commands", 10))
                .commands(command)
            );

            for (int j = 0; j < 10; j++) {
                futures.add(platform.executeAsync("test test 1 100 1.0 1.0f 1 1 true 1s 1d 2021-01-01 00:00:00 2021-01-01 00:00:00"));
            }
        }

        List<AssertExecute> executes = FutureUtil.asList(futures).join();

        assertThat(executes)
            .hasSize(500)
            .allSatisfy(assertExecute -> assertExecute.assertSuccess());
    }

}
