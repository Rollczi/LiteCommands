package dev.rollczi.litecommands.annotations.cooldown;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.cooldown.event.CooldownEvent;
import dev.rollczi.litecommands.unit.annotations.LiteTestSpec;
import java.time.temporal.ChronoUnit;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import org.junit.jupiter.api.Test;

class CooldownAPITest extends LiteTestSpec {

    static CooldownEvent registeredEvent;

    static LiteTestConfig config = builder -> builder
        .listener(CooldownEvent.class, event -> registeredEvent = event);

    @Command(name = "test")
    @Cooldown(key = "test-cooldown", count = 400, unit = ChronoUnit.MILLIS)
    static class TestCommand {

        @Execute
        void execute() {}

        @Execute(name = "with-args")
        @Cooldown(key = "test-cooldown-with-args", count = 600, unit = ChronoUnit.MILLIS)
        void execute(@Arg Integer arg) {}

    }

    @Command(name = "bypass-test")
    @Cooldown(key = "bypass-test-cooldown", count = 400, unit = ChronoUnit.MILLIS, bypass = "test.bypass")
    static class TestWithBypassCommand {

        @Execute
        void execute() {}

    }

    @Test
    void test() {
        platform.execute("test");
        assertThat(registeredEvent)
            .isNotNull();
    }

}
