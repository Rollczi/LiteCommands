package dev.rollczi.litecommands.annotations.cooldown;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import dev.rollczi.litecommands.annotations.LiteTestSpec;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.cooldown.CooldownState;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;

class CooldownAnnotationTest extends LiteTestSpec {

    @Command(name = "test")
    @Cooldown(key = "test-cooldown", count = 1, unit = ChronoUnit.SECONDS)
    static class TestCommand {

        @Execute
        void execute() {}

    }

    @Test
    void test() {
        platform.execute("test");

        CooldownState cooldownState = platform.execute("test")
            .assertFailedAs(CooldownState.class);

        assertEquals("test-cooldown", cooldownState.getCooldownContext().getKey());
        assertEquals(Duration.ofSeconds(1), cooldownState.getCooldownContext().getDuration());
        assertFalse(cooldownState.getRemainingDuration().isZero());

        Awaitility.await()
            .atMost(Duration.ofSeconds(3))
            .until(() -> platform.execute("test").isSuccessful());
    }

}
