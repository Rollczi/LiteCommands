package dev.rollczi.litecommands.jakarta;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.unit.LiteCommandsTestFactory;
import dev.rollczi.litecommands.unit.TestPlatform;
import dev.rollczi.litecommands.unit.TestPlatformSender;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Negative;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Range;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static java.lang.String.format;

class LiteJakartaMessagesTest {

    @Command(name = "test")
    static class TestCommand {

        @Execute(name = "size")
        public void size(@Size(min = 5, max = 10) @Arg("text") String text) {}

        @Execute(name = "range")
        public void range(@Range(min = 5, max = 10) @Arg("number") int number) {}

        @Execute(name = "max")
        public void max(@Max(1) @Arg("number") int number) {}

        @Execute(name = "min")
        public void min(@Min(1) @Arg("number") int number) {}

        @Execute(name = "negative")
        public void negative(@Negative @Arg("number") int number) {}

    }

    TestPlatform platform = LiteCommandsTestFactory.startPlatform(config -> config
        .commands(
            new TestCommand()
        )
        .extension(new LiteJakartaExtension<>(), configuration -> configuration
            // provide locale for jakarta
            .locale(invocation -> {
                Locale property = invocation.platformSender().getProperty(TestPlatformSender.LOCALE);

                return property != null
                    ? property
                    : Locale.ENGLISH;
            })

            // constraint violations message
            .constraintViolationsMessage((invocation, parsedResult) -> "Constraint violations: " + parsedResult.asJoinedString())

            // global violation message
            .violationMessage((invocation, violation) -> "Invalid value for " + violation.getFormattedParameterName())

            // violation messages for specific annotations
            .violationMessage(Size.class, (invocation, violation) -> {
                Size size = violation.getAnnotation();
                return format("Size must be between %d and %d for %s", size.min(), size.max(), violation.getParameterName());
            })

            // violation messages for specific annotations in specific locale
            .violationMessage(Range.class, (invocation, violation) -> {
                Range range = violation.getAnnotation();
                Locale locale = violation.getLocale();

                return Locale.ENGLISH.equals(locale)
                    ? format("[English] Range must be between %d and %d", range.min(), range.max())
                    : format("[Other] Range must be between %d and %d", range.min(), range.max());
            })

        )
    );

    @Test
    @DisplayName("should return configured message specific for @Size annotation")
    void size() {
        platform.execute("test size 1234")
            .assertMessage("Constraint violations: Size must be between 5 and 10 for text");
    }

    @Test
    @DisplayName("should return configured message specific for @Range annotation and locale")
    void range() {
        platform.execute(TestPlatformSender.locale(Locale.ENGLISH), "test range 4")
            .assertMessage("Constraint violations: [English] Range must be between 5 and 10");

        platform.execute(TestPlatformSender.locale(Locale.GERMAN), "test range 4")
            .assertMessage("Constraint violations: [Other] Range must be between 5 and 10");
    }

    @Test
    @DisplayName("should return configured message specific for @Max annotation")
    void max() {
        platform.execute("test max 2")
            .assertMessage("Constraint violations: Invalid value for <number>");
    }

    @Test
    @DisplayName("should return configured message specific for @Min annotation")
    void min() {
        platform.execute("test min 0")
            .assertMessage("Constraint violations: Invalid value for <number>");
    }

}
