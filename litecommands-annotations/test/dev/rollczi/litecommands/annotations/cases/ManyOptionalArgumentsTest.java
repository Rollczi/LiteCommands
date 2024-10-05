package dev.rollczi.litecommands.annotations.cases;

import dev.rollczi.litecommands.unit.annotations.LiteTestSpec;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.flag.Flag;
import dev.rollczi.litecommands.annotations.varargs.Varargs;
import dev.rollczi.litecommands.argument.resolver.standard.DurationArgumentResolver;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class ManyOptionalArgumentsTest extends LiteTestSpec {

    enum TestEnum {
        A, B, C
    }

    @Command(name = "test")
    static class TestCommand {
        @Execute(name = "with-space")
        String test(@Varargs(delimiter = ", ") List<TestEnum> enums, @Flag("-s") boolean silent, @Arg Duration duration) {
            if (enums.isEmpty()) {
                return String.format("Silent: %s, Duration: %s", silent, duration);
            }

            String enumsAsString = enums.stream().map(testEnum -> testEnum.name()).collect(Collectors.joining(", "));
            return String.format("Enums: %s, Silent: %s, Duration: %s", enumsAsString, silent, duration);
        }

        @Execute(name = "without-space")
        String testWithoutSpace(@Varargs(delimiter = ",") List<TestEnum> enums, @Flag("-s") boolean silent, @Arg Duration duration) {
            if (enums.isEmpty()) {
                return String.format("Silent: %s, Duration: %s", silent, duration);
            }

            String enumsAsString = enums.stream().map(testEnum -> testEnum.name()).collect(Collectors.joining(", "));
            return String.format("Enums: %s, Silent: %s, Duration: %s", enumsAsString, silent, duration);
        }
    }

    @Test
    void testExecuteWithSpace() {
        platform.execute("test with-space A, B, C -s 1h")
            .assertSuccess("Enums: A, B, C, Silent: true, Duration: PT1H");

        platform.execute("test with-space -s 1h")
            .assertSuccess("Silent: true, Duration: PT1H");

        platform.execute("test with-space 1h")
            .assertSuccess("Silent: false, Duration: PT1H");

        platform.execute("test with-space A, B, C 1h")
            .assertSuccess("Enums: A, B, C, Silent: false, Duration: PT1H");
    }

    @Test
    void testSuggestionsWithSpace() {
        platform.suggest("test with-space A")
            .assertSuggest("A", "A, ");

        platform.suggest("test with-space A, ")
            .assertSuggest("A", "B", "C");

        platform.suggest("test with-space A, B, C ")
            .assertSuggestAndFlush("-s")
            .assertAsSuggester(new DurationArgumentResolver<>(), "");

        platform.suggest("test with-space A, B, C -s ")
            .assertAsSuggester(new DurationArgumentResolver<>(), "");

        platform.suggest("test with-space A, B, C -s 1")
            .assertAsSuggester(new DurationArgumentResolver<>(), "1");
    }

    @Test
    void testExecuteWithoutSpace() {
        platform.execute("test without-space A,B,C -s 1h")
            .assertSuccess("Enums: A, B, C, Silent: true, Duration: PT1H");

        platform.execute("test without-space -s 1h")
            .assertSuccess("Silent: true, Duration: PT1H");

        platform.execute("test without-space 1h")
            .assertSuccess("Silent: false, Duration: PT1H");

        platform.execute("test without-space A,B,C 1h")
            .assertSuccess("Enums: A, B, C, Silent: false, Duration: PT1H");
    }

    @Test
    void testSuggestionsWithoutSpace() {
        platform.suggest("test without-space A")
            .assertSuggest("A", "A,");

        platform.suggest("test without-space A,")
            .assertSuggest("A,A", "A,B", "A,C");

        platform.suggest("test without-space A,B,C ")
            .assertSuggestAndFlush("-s")
            .assertAsSuggester(new DurationArgumentResolver<>(), "");

        platform.suggest("test without-space A,B,C -s ")
            .assertAsSuggester(new DurationArgumentResolver<>(), "");

        platform.suggest("test without-space A,B,C -s 1")
            .assertAsSuggester(new DurationArgumentResolver<>(), "1");
    }

}



