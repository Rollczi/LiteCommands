package dev.rollczi.litecommands.annotations.cooldown;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import dev.rollczi.litecommands.annotations.LiteTestSpec;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.cooldown.CooldownState;
import dev.rollczi.litecommands.platform.PlatformSender;
import dev.rollczi.litecommands.unit.TestPlatformSender;
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

    @Command(name = "bypass-test")
    @Cooldown(key = "bypass-test-cooldown", count = 1, unit = ChronoUnit.SECONDS, bypass = "test.bypass")
    static class TestWithBypassCommand {

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

    @Test
    void testCooldownBypass() {
        PlatformSender permittedSender = TestPlatformSender.permitted("test.bypass");
        platform.execute(permittedSender, "bypass-test").assertSuccess();
        platform.execute(permittedSender, "bypass-test").assertSuccess();

        platform.execute("bypass-test");

        CooldownState cooldownState = platform.execute("bypass-test")
            .assertFailedAs(CooldownState.class);

        assertEquals("bypass-test-cooldown", cooldownState.getCooldownContext().getKey());
        assertEquals(Duration.ofSeconds(1), cooldownState.getCooldownContext().getDuration());
        assertFalse(cooldownState.getRemainingDuration().isZero());

        Awaitility.await()
            .atMost(Duration.ofSeconds(3))
            .until(() -> platform.execute("bypass-test").isSuccessful());
    }

}
