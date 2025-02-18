package dev.rollczi.litecommands.annotations.cooldown;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.invalidusage.InvalidUsage;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import dev.rollczi.litecommands.unit.annotations.LiteTestSpec;
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

        CooldownState cooldownState = platform.execute("test")
            .assertFailedAs(CooldownState.class);

        assertEquals("test-cooldown", cooldownState.getKey());
        assertEquals(Duration.ofMillis(400), cooldownState.getDuration());
        assertFalse(cooldownState.getRemainingDuration().isZero());

        Awaitility.await()
            .atMost(Duration.ofSeconds(3))
            .until(() -> platform.execute("test").isSuccessful());
    }

    @Test
    void testInvalidUsage() {
        platform.execute("test with-args invalid")
            .assertFailureInvalid(InvalidUsage.Cause.INVALID_ARGUMENT);

        platform.execute("test with-args invalid")
            .assertFailureInvalid(InvalidUsage.Cause.INVALID_ARGUMENT);

        platform.execute("test with-args invalid")
            .assertFailureInvalid(InvalidUsage.Cause.INVALID_ARGUMENT);

        platform.execute("test with-args 10")
            .assertSuccess();

        CooldownState cooldownState = platform.execute("test with-args 10")
            .assertFailedAs(CooldownState.class);

        assertEquals("test-cooldown-with-args", cooldownState.getKey());
        assertEquals(Duration.ofMillis(600), cooldownState.getDuration());
        assertFalse(cooldownState.getRemainingDuration().isZero());

        Awaitility.await()
            .atMost(Duration.ofSeconds(3))
            .until(() -> platform.execute("test with-args 10").isSuccessful());
    }

    @Test
    void testCooldownBypass() {
        PlatformSender permittedSender = TestPlatformSender.permitted("test.bypass");
        platform.execute(permittedSender, "bypass-test").assertSuccess();
        platform.execute(permittedSender, "bypass-test").assertSuccess();

        platform.execute("bypass-test");

        CooldownState cooldownState = platform.execute("bypass-test")
            .assertFailedAs(CooldownState.class);

        assertEquals("bypass-test-cooldown", cooldownState.getKey());
        assertEquals(Duration.ofMillis(400), cooldownState.getDuration());
        assertFalse(cooldownState.getRemainingDuration().isZero());

        Awaitility.await()
            .atMost(Duration.ofSeconds(3))
            .until(() -> platform.execute("bypass-test").isSuccessful());
    }

}
