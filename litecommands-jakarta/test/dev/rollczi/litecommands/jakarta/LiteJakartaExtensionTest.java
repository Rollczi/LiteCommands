package dev.rollczi.litecommands.jakarta;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.unit.LiteCommandsTestFactory;
import dev.rollczi.litecommands.unit.TestPlatform;
import jakarta.validation.constraints.AssertFalse;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Negative;
import jakarta.validation.constraints.NegativeOrZero;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.hibernate.validator.constraints.Range;
import org.junit.jupiter.api.Test;

import static java.lang.String.format;
import static java.time.temporal.ChronoUnit.MINUTES;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;

class LiteJakartaExtensionTest {

    @Command(name = "test")
    static class TestCommand {

        @Execute(name = "size")
        public void size(@Size(min = 5, max = 10) @Arg String text) {}

        @Execute(name = "range")
        public void range(@Range(min = 5, max = 10) @Arg int number) {}

        @Execute(name = "max")
        public void max(@Max(1) @Arg int number) {}

        @Execute(name = "min")
        public void min(@Min(1) @Arg int number) {}

        @Execute(name = "negative")
        public void negative(@Negative @Arg int number) {}

        @Execute(name = "negative-or-zero")
        public void negativeOrZero(@NegativeOrZero @Arg int number) {}

        @Execute(name = "past")
        public void past(@Past @Arg Instant instant) {}

        @Execute(name = "past-or-present")
        public void pastOrPresent(@PastOrPresent @Arg Instant instant) {}

        @Execute(name = "positive")
        public void positive(@Positive @Arg int number) {}

        @Execute(name = "positive-or-zero")
        public void positiveOrZero(@PositiveOrZero @Arg int number) {}

        @Execute(name = "pattern")
        public void pattern(@Pattern(regexp = "[a-z]+") @Arg String text) {}

        @Execute(name = "decimal-max")
        public void decimalMax(@DecimalMax("1.0") @Arg double number) {}

        @Execute(name = "decimal-min")
        public void decimalMin(@DecimalMin("1.0") @Arg double number) {}

        @Execute(name = "digits")
        public void digits(@Digits(integer = 2, fraction = 2) @Arg double number) {}

        @Execute(name = "email")
        public void email(@Email @Arg String email) {}

        @Execute(name = "future")
        public void future(@Future @Arg Instant instant) {}

        @Execute(name = "future-or-present")
        public void futureOrPresent(@FutureOrPresent @Arg Instant instant) {}

        @Execute(name = "assert-false")
        public void assertFalse(@AssertFalse @Arg boolean bool) {}

        @Execute(name = "assert-true")
        public void assertTrue(@AssertTrue @Arg boolean bool) {}

    }

    TestPlatform platform = LiteCommandsTestFactory.startPlatform(config -> config
        .commands(
            new TestCommand()
        )
        .extension(new LiteJakartaExtension<>())
    );

    @Test
    void sizeValidationShouldSucceed() {
        platform.execute("test size long-text")
            .assertSuccess();
    }

    @Test
    void sizeValidationShouldFail() {
        JakartaRawResult testText = platform.execute("test size text")
            .assertFailedAs(JakartaRawResult.class);

        assertThat(testText.getViolations()).hasSize(1);
    }

    @Test
    void rangeValidationShouldSucceed() {
        platform.execute("test range 10")
            .assertSuccess();
    }

    @Test
    void rangeValidationShouldFail() {
        platform.execute("test range 100")
            .assertFailedAs(JakartaRawResult.class);
    }

    @Test
    void maxValidationShouldSucceed() {
        platform.execute("test max 0")
            .assertSuccess();
    }

    @Test
    void maxValidationShouldFail() {
        platform.execute("test max 2")
            .assertFailedAs(JakartaRawResult.class);
    }

    @Test
    void minValidationShouldSucceed() {
        platform.execute("test min 2")
            .assertSuccess();
    }

    @Test
    void minValidationShouldFail() {
        platform.execute("test min 0")
            .assertFailedAs(JakartaRawResult.class);
    }

    @Test
    void negativeValidationShouldSucceed() {
        platform.execute("test negative -1")
            .assertSuccess();
    }

    @Test
    void negativeValidationShouldFail() {
        platform.execute("test negative 1")
            .assertFailedAs(JakartaRawResult.class);
    }

    @Test
    void negativeOrZeroValidationShouldSucceed() {
        platform.execute("test negative-or-zero -1")
            .assertSuccess();
    }

    @Test
    void negativeOrZeroValidationShouldSucceedWithZero() {
        platform.execute("test negative-or-zero 0")
            .assertSuccess();
    }

    @Test
    void negativeOrZeroValidationShouldFail() {
        platform.execute("test negative-or-zero 1")
            .assertFailedAs(JakartaRawResult.class);
    }

    @Test
    void pastValidationShouldSucceed() {
        platform.execute(format("test past %s", normalizeInstant(Instant.EPOCH)))
            .assertSuccess();
    }

    @Test
    void pastValidationShouldFail() {
        platform.execute(format("test past %s", normalizeInstant(Instant.now().plus(1, ChronoUnit.MINUTES))))
            .assertFailedAs(JakartaRawResult.class);
    }

    @Test
    void pastOrPresentValidationShouldSucceed() {
        platform.execute(format("test past-or-present %s", normalizeInstant(Instant.now())))
            .assertSuccess();
    }

    @Test
    void pastOrPresentValidationShouldFail() {
        platform.execute(format("test past-or-present %s", normalizeInstant(Instant.now().plus(1, MINUTES))))
            .assertFailedAs(JakartaRawResult.class);
    }

    private String normalizeInstant(Instant instant) {
        return instant.truncatedTo(SECONDS)
            .toString()
            .replace("T", " ")
            .replace("Z", "");
    }

    @Test
    void positiveValidationShouldSucceed() {
        platform.execute("test positive 1")
            .assertSuccess();
    }

    @Test
    void positiveValidationShouldFail() {
        platform.execute("test positive -1")
            .assertFailedAs(JakartaRawResult.class);
    }

    @Test
    void positiveOrZeroValidationShouldSucceed() {
        platform.execute("test positive-or-zero 0")
            .assertSuccess();
    }

    @Test
    void positiveOrZeroValidationShouldFail() {
        platform.execute("test positive-or-zero -1")
            .assertFailedAs(JakartaRawResult.class);
    }

    @Test
    void patternValidationShouldSucceed() {
        platform.execute("test pattern abc")
            .assertSuccess();
    }

    @Test
    void patternValidationShouldFail() {
        platform.execute("test pattern 123")
            .assertFailedAs(JakartaRawResult.class);
    }

    @Test
    void decimalMaxValidationShouldSucceed() {
        platform.execute("test decimal-max 1.0")
            .assertSuccess();
    }

    @Test
    void decimalMaxValidationShouldFail() {
        platform.execute("test decimal-max 1.1")
            .assertFailedAs(JakartaRawResult.class);
    }

    @Test
    void decimalMinValidationShouldSucceed() {
        platform.execute("test decimal-min 1.0")
            .assertSuccess();
    }

    @Test
    void decimalMinValidationShouldFail() {
        platform.execute("test decimal-min 0.9")
            .assertFailedAs(JakartaRawResult.class);
    }

    @Test
    void digitValidationShouldSucceed() {
        platform.execute("test digits 1.12")
            .assertSuccess();
    }

    @Test
    void digitValidationShouldFail() {
        platform.execute("test digits 1.123")
            .assertFailedAs(JakartaRawResult.class);
    }

    @Test
    void emailValidationShouldSucceed() {
        platform.execute("test email hello@rafal.moe")
            .assertSuccess();
    }

    @Test
    void emailValidationShouldFail() {
        platform.execute("test email for_sure_not_email")
            .assertFailedAs(JakartaRawResult.class);
    }

    @Test
    void futureValidationShouldSucceed() {
        platform.execute(format("test future %s", normalizeInstant(Instant.now().plus(1, MINUTES))))
            .assertSuccess();
    }

    @Test
    void futureValidationShouldFail() {
        platform.execute(format("test future %s", normalizeInstant(Instant.now().minus(1, MINUTES))))
            .assertFailedAs(JakartaRawResult.class);
    }

    @Test
    void futureOrPresentValidationShouldSucceed() {
        platform.execute(format("test future-or-present %s", normalizeInstant(Instant.now().plus(30, SECONDS))))
            .assertSuccess();
    }

    @Test
    void futureOrPresentValidationShouldFail() {
        platform.execute(format("test future-or-present %s", normalizeInstant(Instant.now().minus(1, MINUTES))))
            .assertFailedAs(JakartaRawResult.class);
    }

    @Test
    void assertFalseValidationShouldSucceed() {
        platform.execute("test assert-false false")
            .assertSuccess();
    }

    @Test
    void assertFalseValidationShouldFail() {
        platform.execute("test assert-false true")
            .assertFailedAs(JakartaRawResult.class);
    }

    @Test
    void assertTrueValidationShouldSucceed() {
        platform.execute("test assert-true true")
            .assertSuccess();
    }

    @Test
    void assertTrueValidationShouldFail() {
        platform.execute("test assert-true false")
            .assertFailedAs(JakartaRawResult.class);
    }

}
