package dev.rollczi.litecommands.annotations.argument.resolver.collector;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.varargs.Varargs;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.resolver.standard.DurationArgumentResolver;
import dev.rollczi.litecommands.argument.resolver.standard.EnumArgumentResolver;
import dev.rollczi.litecommands.argument.resolver.standard.NumberArgumentResolver;
import dev.rollczi.litecommands.invalidusage.InvalidUsage.Cause;
import dev.rollczi.litecommands.reflect.type.TypeToken;
import dev.rollczi.litecommands.unit.annotations.LiteTestSpec;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class VarargsArgumentTest extends LiteTestSpec {

    private final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        .withZone(ZoneOffset.UTC);

    private static Stream<Arguments> delimiters() {
        return Stream.of(
            Arguments.of("test normal", ","),
            Arguments.of("test with-space", ", "),
            Arguments.of("test custom", " | ")
        );
    }

    private static final Argument<TestEnum> TEST_ENUM_ARGUMENT = Argument.of("test", TypeToken.of(TestEnum.class));

    enum TestEnum {
        FIRST,
        SECOND,
        THIRD,
        FOURTH
    }

    record Pair<A, B>(A first, B second) {
        static <A, B> Pair<A, B> of(A first, B second) {
            return new Pair<>(first, second);
        }
    }

    @Command(name = "test normal")
    static class TestCommand {
        @Execute(name = "Integer")
        List<Integer> testInteger(@Varargs(delimiter = ",") List<Integer> test) {
            return test;
        }

        @Execute(name = "String")
        List<String> testString(@Varargs(value = "argument", delimiter = ",") List<String> test) {
            return test;
        }

        @Execute(name = "Duration")
        List<Duration> testDuration(@Varargs(delimiter = ",") List<Duration> test) {
            return test;
        }

        @Execute(name = "Instant")
        List<Instant> testInstant(@Varargs(delimiter = ",") List<Instant> test) {
            return test;
        }

        @Execute(name = "Instant-special")
        Pair<List<Instant>, Integer> testInstantSpecial(@Varargs(delimiter = ",") List<Instant> test, @Arg int a) {
            return Pair.of(test, a);
        }

        @Execute(name = "enum")
        List<TestEnum> testTestEnum(@Varargs(delimiter = ",") List<TestEnum> test) {
            return test;
        }

        @Execute(name = "durations-instant")
        Pair<List<Duration>, Instant> testInstantDuration(@Varargs(delimiter = ",") List<Duration> durations, @Arg Instant instant) {
            return Pair.of(durations, instant);
        }

        @Execute(name = "instants-duration")
        Pair<List<Instant>, Duration> testDurationInstant(@Varargs(delimiter = ",") List<Instant> instants, @Arg Duration duration) {
            return Pair.of(instants, duration);
        }
    }

    @Command(name = "test with-space")
    static class TestWithSpaceCommand {
        @Execute(name = "Integer")
        List<Integer> testInteger(@Varargs(delimiter = ", ") List<Integer> test) {
            return test;
        }

        @Execute(name = "String")
        List<String> testString(@Varargs(value = "argument", delimiter = ", ") List<String> test) {
            return test;
        }

        @Execute(name = "Duration")
        List<Duration> testDuration(@Varargs(delimiter = ", ") List<Duration> test) {
            return test;
        }

        @Execute(name = "Instant")
        List<Instant> testInstant(@Varargs(delimiter = ", ") List<Instant> test) {
            return test;
        }

        @Execute(name = "Instant-special")
        Pair<List<Instant>, Integer> testInstantSpecial(@Varargs(delimiter = ", ") List<Instant> test, @Arg int a) {
            return Pair.of(test, a);
        }

        @Execute(name = "enum")
        List<TestEnum> testTestEnum(@Varargs(delimiter = ", ") List<TestEnum> test) {
            return test;
        }

        @Execute(name = "durations-instant")
        Pair<List<Duration>, Instant> testInstantDuration(@Varargs(delimiter = ", ") List<Duration> durations, @Arg Instant instant) {
            return Pair.of(durations, instant);
        }

        @Execute(name = "instants-duration")
        Pair<List<Instant>, Duration> testDurationInstant(@Varargs(delimiter = ", ") List<Instant> instants, @Arg Duration duration) {
            return Pair.of(instants, duration);
        }
    }

    @Command(name = "test custom")
    static class TestCustomCommand {
        @Execute(name = "Integer")
        List<Integer> testInteger(@Varargs(delimiter = " | ") List<Integer> test) {
            return test;
        }

        @Execute(name = "String")
        List<String> testString(@Varargs(value = "argument", delimiter = " | ") List<String> test) {
            return test;
        }

        @Execute(name = "Duration")
        List<Duration> testDuration(@Varargs(delimiter = " | ") List<Duration> test) {
            return test;
        }

        @Execute(name = "Instant")
        List<Instant> testInstant(@Varargs(delimiter = " | ") List<Instant> test) {
            return test;
        }

        @Execute(name = "Instant-special")
        Pair<List<Instant>, Integer> testInstantSpecial(@Varargs(delimiter = " | ") List<Instant> test, @Arg int a) {
            return Pair.of(test, a);
        }

        @Execute(name = "enum")
        List<TestEnum> testTestEnum(@Varargs(delimiter = " | ") List<TestEnum> test) {
            return test;
        }

        @Execute(name = "durations-instant")
        Pair<List<Duration>, Instant> testInstantDuration(@Varargs(delimiter = " | ") List<Duration> durations, @Arg Instant instant) {
            return Pair.of(durations, instant);
        }

        @Execute(name = "instants-duration")
        Pair<List<Instant>, Duration> testDurationInstant(@Varargs(delimiter = " | ") List<Instant> instants, @Arg Duration duration) {
            return Pair.of(instants, duration);
        }
    }

    @ParameterizedTest
    @MethodSource("delimiters")
    @DisplayName("Test parsing multiple arguments with different delimiters")
    void testParsing(String command, String del) {
        platform.execute(command + " Integer 1" + del + "2" + del + "3" + del + "4" + del + "5")
            .assertSuccess(Arrays.asList(1, 2, 3, 4, 5));

        platform.execute(command + " String text1" + del + "text2" + del + "text3" + del + "text4" + del + "text5")
            .assertSuccess(Arrays.asList("text1", "text2", "text3", "text4", "text5"));

        platform.execute(command + " Duration 10m" + del + "1h2m3s" + del + "1h" + del + "1d3h" + del + "2d")
            .assertSuccess(Arrays.asList(
                Duration.ofMinutes(10),
                Duration.ofHours(1).plusMinutes(2).plusSeconds(3),
                Duration.ofHours(1),
                Duration.ofDays(1).plusHours(3),
                Duration.ofDays(2)
            ));

        platform.execute(command + " Instant 2021-01-01 00:05:50" + del + "2023-04-17 11:03:00" + del + "2016-12-31 23:59:59")
            .assertSuccess(Arrays.asList(
                Instant.parse("2021-01-01T00:05:50Z"),
                Instant.parse("2023-04-17T11:03:00Z"),
                Instant.parse("2016-12-31T23:59:59Z")
            ));

        platform.execute(command + " Instant-special 2021-01-01 00:05:50" + del + "2023-04-17 11:03:00" + del + "2016-12-31 23:59:59 5")
            .assertSuccess(Pair.of(Arrays.asList(
                Instant.parse("2021-01-01T00:05:50Z"),
                Instant.parse("2023-04-17T11:03:00Z"),
                Instant.parse("2016-12-31T23:59:59Z")
            ), 5));

        platform.execute(command + " enum FIRST" + del + "SECOND" + del + "THIRD" + del + "FOURTH")
            .assertSuccess(Arrays.asList(
                TestEnum.FIRST,
                TestEnum.SECOND,
                TestEnum.THIRD,
                TestEnum.FOURTH
            ));
    }

    @ParameterizedTest
    @MethodSource("delimiters")
    @DisplayName("Test parsing single argument with different delimiters")
    void testParsingSingle(String command, String delimiter) {
        platform.execute(command + " Integer 1")
            .assertSuccess(Collections.singletonList(1));

        platform.execute(command + " String text")
            .assertSuccess(Collections.singletonList("text"));

        platform.execute(command + " Duration 1h")
            .assertSuccess(Collections.singletonList(Duration.ofHours(1)));

        platform.execute(command + " Instant 2021-01-01 00:05:50")
            .assertSuccess(Collections.singletonList(Instant.parse("2021-01-01T00:05:50Z")));

        platform.execute(command + " enum FIRST")
            .assertSuccess(Collections.singletonList(TestEnum.FIRST));
    }

    @ParameterizedTest
    @MethodSource("delimiters")
    @DisplayName("Test parsing invalid arguments with different delimiters")
    void testParsingInvalidArguments(String command, String del) {
        platform.execute(command + " Integer 1acab")
            .assertFailureInvalid(Cause.INVALID_ARGUMENT);

        platform.execute(command + " Integer 1" + del + "a" + del + "b")
            .assertFailureInvalid(Cause.INVALID_ARGUMENT);

        platform.execute(command + " Duration 10m" + del + "1h2m3s" + del + "Invalid" + del + "1d3h" + del + "2d" + del + "1h")
            .assertFailureInvalid(Cause.INVALID_ARGUMENT);

        platform.execute(command + " Duration 10m" + del + "1h2m3s" + del + "3" + del + "1d3h" + del + "2d" + del + "1h")
            .assertFailureInvalid(Cause.INVALID_ARGUMENT);

        platform.execute(command + " enum FIRST" + del + "SECOND" + del + "THIRD" + del + "FOURTH" + del + "Invalid")
            .assertFailureInvalid(Cause.INVALID_ARGUMENT);

        platform.execute(command + " enum FIRST" + del + "SECOND" + del + "THIRD" + del + "FOURTH" + del + "1")
            .assertFailureInvalid(Cause.INVALID_ARGUMENT);

        platform.execute(command + " Instant 2021-01-01 Invalid")
            .assertFailure("Invalid date format '2021-01-01 Invalid'! Use: <yyyy-MM-dd> <HH:mm:ss> (INSTANT_INVALID_FORMAT)");

        platform.execute(command + " Instant 2021-01-01 00:05:50" + del + "2023-04-17 Invalid" + del + "2016-12-31 23:59:59")
            .assertFailure("Invalid date format '2023-04-17 Invalid'! Use: <yyyy-MM-dd> <HH:mm:ss> (INSTANT_INVALID_FORMAT)");

        platform.execute(command + " Instant Invalid 00:05:50" + del + "2023-04-17 00:05:50" + del + "2016-12-31 23:59:59")
            .assertFailure("Invalid date format 'Invalid 00:05:50'! Use: <yyyy-MM-dd> <HH:mm:ss> (INSTANT_INVALID_FORMAT)");

        platform.execute(command + " Instant 2021-01-01 00:05:50" + del + "2023-04-17 11:03:00" + del + "2016-12-31 ")
            .assertFailure("Invalid date format '2016-12-31 '! Use: <yyyy-MM-dd> <HH:mm:ss> (INSTANT_INVALID_FORMAT)");

        platform.execute(command + " Instant 2021-01-01 00:05:50" + del + "2023-04-17 11:03:00" + del)
            .assertFailureInvalid(Cause.MISSING_PART_OF_ARGUMENT);
    }

    @ParameterizedTest
    @MethodSource("delimiters")
    @DisplayName("Test parsing empty arguments with different delimiters")
    void testEmpty(String command, String delimiter) {
        platform.execute(command + " Integer")
            .assertSuccess(Collections.emptyList());

        platform.execute(command + " Integer ")
            .assertSuccess(Collections.emptyList());

        platform.execute(command + " String")
            .assertSuccess(Collections.emptyList());

        platform.execute(command + " String ")
            .assertSuccess(Collections.singletonList(""));

        platform.execute(command + " Duration")
            .assertSuccess(Collections.emptyList());

        platform.execute(command + " Duration ")
            .assertSuccess(Collections.emptyList());

        platform.execute(command + " Instant")
            .assertSuccess(Collections.emptyList());

        platform.execute(command + " Instant ")
            .assertSuccess(Collections.emptyList());

        platform.execute(command + " enum")
            .assertSuccess(Collections.emptyList());

        platform.execute(command + " enum ")
            .assertSuccess(Collections.emptyList());
    }

    @ParameterizedTest
    @MethodSource("delimiters")
    @DisplayName("Test suggest integer with different delimiters")
    void testSuggestInteger(String command, String delimiter) {
        platform.suggest(command + " Integer ")
            .assertAsSuggester(NumberArgumentResolver.ofInteger(), "");

        platform.suggest(command + " Integer 1" + delimiter)
            .mapIf(delimiter.contains(" "), s -> "1" + delimiter + s)
            .assertAsSuggester(NumberArgumentResolver.ofInteger(), s -> "1" + delimiter + s, "");

        platform.suggest(command + " Integer 5")
            .assertSuggestAndFlush("5" + delimiter, "5")
            .assertAsSuggester(NumberArgumentResolver.ofInteger(), "5");
    }

    @ParameterizedTest
    @MethodSource("delimiters")
    @DisplayName("Test suggest string with different delimiters")
    void testSuggestString(String command, String delimiter) {
        platform.suggest(command + " String ")
            .assertSuggest("<argument>");
        platform.suggest(command + " String <argument>")
            .assertSuggest("<argument>", "<argument>" + delimiter);
        platform.suggest(command + " String text" + delimiter)
            .mapIf(delimiter.contains(" "), s -> "text" + delimiter + s)
            .assertSuggest("text" + delimiter, "text" + delimiter + "<argument>");

        platform.suggest(command + " String text")
            .assertSuggest("text", "text" + delimiter);
    }

    @ParameterizedTest
    @MethodSource("delimiters")
    @DisplayName("Test suggest Duration with different delimiters")
    void testSuggestDuration(String command, String del) {
        platform.suggest(command + " Duration ")
            .assertAsSuggester(new DurationArgumentResolver<>(), "");

        platform.suggest(command + " Duration 10")
            .assertAsSuggester(new DurationArgumentResolver<>(), "10");

        platform.suggest(command + " Duration 1m")
            .assertSuggestAndFlush("1m" + del)
            .assertAsSuggester(new DurationArgumentResolver<>(), "1m");

        platform.suggest(command + " Duration 10h" + del)
            .mapIf(del.contains(" "), s -> "10h" + del + s)
            .assertAsSuggester(new DurationArgumentResolver<>(), s -> "10h" + del + s, "");

        platform.suggest(command + " Duration 10h" + del + "1")
            .mapIf(del.contains(" "), s -> "10h" + del + s)
            .assertAsSuggester(new DurationArgumentResolver<>(), s -> "10h" + del + s, "1");

        platform.suggest(command + " Duration 10h" + del + "1m")
            .mapIf(del.contains(" "), s -> "10h" + del + s)
            .assertSuggestAndFlush("10h" + del + "1m" + del)
            .assertAsSuggester(new DurationArgumentResolver<>(), s -> "10h" + del + s, "1m");

        platform.suggest(command + " Duration 10h" + del + "1m" + del)
            .mapIf(del.contains(" "), s -> "10h" + del + "1m" + del + s)
            .assertAsSuggester(new DurationArgumentResolver<>(), s -> "10h" + del + "1m" + del + s, "");

        platform.suggest(command + " Duration 10h" + del + "1m" + del + "1")
            .mapIf(del.contains(" "), s -> "10h" + del + "1m" + del + s)
            .assertAsSuggester(new DurationArgumentResolver<>(), s -> "10h" + del + "1m" + del + s, "1");

        platform.suggest(command + " Duration 10h" + del + "1m" + del + "5m")
            .mapIf(del.contains(" "), s -> "10h" + del + "1m" + del + s)
            .assertSuggestAndFlush("10h" + del + "1m" + del + "5m" + del)
            .assertAsSuggester(new DurationArgumentResolver<>(), s -> "10h" + del + "1m" + del + s, "5m");
    }

    @ParameterizedTest
    @MethodSource("delimiters")
    @DisplayName("Test suggest Instant list with different delimiters with additional int argument at the end of the command")
    void testSuggestInstantSpecial(String command, String del) {
        platform.suggest(command + " Instant-special ")
            .assertNotEmpty()
            .assertCorrect(suggestion -> {
                String multilevel = suggestion.multilevel();
                if ("-".equals(multilevel)) {
                    return;
                }

                if (multilevel.chars().allMatch(Character::isDigit)) {
                    platform.execute(command + " Instant-special " + multilevel).assertSuccess();
                    return;
                }
                platform.execute(command + " Instant-special " + multilevel + " 5").assertSuccess();
            });
    }

    @ParameterizedTest
    @MethodSource("delimiters")
    @DisplayName("Test suggest Duration list with different delimiters with additional Instant argument at the end of the command")
    void testSuggestDurationInstant(String command, String del) {
        platform.suggest(command + " durations-instant ")
            .assertNotEmpty()
            .assertSuggestAndFlush(DurationArgumentResolver.SUGGESTIONS_LIST)
            .assertCorrect(suggestion -> {
                String multilevel = suggestion.multilevel();
                if ("-".equals(multilevel)) {
                    return;
                }

                Instant instant = Instant.from(dateTimeFormatter.parse(multilevel));

                platform.execute(command + " durations-instant " + multilevel)
                    .assertSuccess(Pair.of(Collections.emptyList(), instant));

                platform.execute(command + " durations-instant 10s " + multilevel)
                    .assertSuccess(Pair.of(Collections.singletonList(Duration.ofSeconds(10)), instant));

                platform.execute(command + " durations-instant 10s" + del + "31m " + multilevel)
                    .assertSuccess(Pair.of(Arrays.asList(Duration.ofSeconds(10), Duration.ofMinutes(31)), instant));
            });

        platform.suggest(command + " durations-instant 10s" + del)
            .mapIf(del.contains(" "), s -> "10s" + del + s)
            .assertAsSuggester(new DurationArgumentResolver<>(), s -> "10s" + del + s, "");

        platform.suggest(command + " durations-instant 10s" + del + "31m ")
            .assertNotEmpty()
            .assertCorrect(suggestion -> {
                String multilevel = suggestion.multilevel();
                Instant instant = Instant.from(dateTimeFormatter.parse(multilevel));

                platform.execute(command + " durations-instant 10s" + del + "31m " + multilevel)
                    .assertSuccess(Pair.of(Arrays.asList(Duration.ofSeconds(10), Duration.ofMinutes(31)), instant));
            });
    }

    @ParameterizedTest
    @MethodSource("delimiters")
    @DisplayName("Test suggest Instant list with different delimiters with additional Duration argument at the end of the command")
    void testSuggestInstantDuration(String command, String del) {
        platform.suggest(command + " instants-duration ")
            .assertSuggestAndFlush(DurationArgumentResolver.SUGGESTIONS_LIST)
            .assertNotEmpty()
            .assertCorrect(suggestion -> {
                String multilevel = suggestion.multilevel();
                Instant instant = Instant.from(dateTimeFormatter.parse(multilevel));
                platform.execute(command + " instants-duration " + multilevel + " 10s")
                    .assertSuccess(Pair.of(Collections.singletonList(instant), Duration.ofSeconds(10)));

                platform.execute(command + " instants-duration " + multilevel + del + multilevel + " 10s")
                    .assertSuccess(Pair.of(Arrays.asList(instant, instant), Duration.ofSeconds(10)));
            });

        platform.suggest(command + " instants-duration 2021-01-01 ")
            .assertNotEmpty()
            .assertCorrect(suggestion -> {
                String multilevel = suggestion.multilevel();
                Instant instant = Instant.from(dateTimeFormatter.parse("2021-01-01 " + multilevel));

                platform.execute(command + " instants-duration 2021-01-01 " + multilevel + " 10s")
                    .assertSuccess(Pair.of(Collections.singletonList(instant), Duration.ofSeconds(10)));
            });

        platform.suggest(command + " instants-duration 2021-01-01 00:05:50 ")
            .assertNotEmpty()
            .assertAsSuggester(new DurationArgumentResolver<>(), "");
    }

    @ParameterizedTest
    @MethodSource("delimiters")
    @DisplayName("Test suggest Instant with different delimiters")
    void testSuggestInstant(String command, String del) {
        platform.suggest(command + " Instant ")
            .assertNotEmpty()
            .assertCorrect(suggestion -> platform.execute(command + " Instant " + suggestion.multilevel()).assertSuccess());

        LocalDate tomorrow = LocalDate.now().plusDays(1);
        String tomorrowFormat = dateFormat.format(tomorrow);

        platform.suggest(command + " Instant " + tomorrow.getYear())
            .assertNotEmpty()
            .assertCorrect(suggestion -> platform.execute(command + " Instant " + suggestion.multilevel()).assertSuccess());

        platform.suggest(command + " Instant " + tomorrowFormat + " ")
            .assertNotEmpty()
            .assertCorrect(suggestion -> platform.execute(command + " Instant " + tomorrowFormat + " " + suggestion.multilevel()).assertSuccess());

        platform.suggest(command + " Instant 2021-01-01 00:05:50" + del + "2023-04-17 11:03:00" + del + tomorrowFormat + " ")
            .assertNotEmpty()
            .assertCorrect(suggestion -> platform.execute(command + " Instant " + tomorrowFormat + " " + suggestion.multilevel()).assertSuccess());
    }

    @ParameterizedTest
    @MethodSource("delimiters")
    @DisplayName("Test suggest enum with different delimiters")
    void testSuggestEnum(String command, String del) {
        platform.suggest(command + " enum ")
            .assertSuggest("FIRST", "SECOND", "THIRD", "FOURTH");

        platform.suggest(command + " enum FIRST" + del)
            .mapIf(del.contains(" "), s -> "FIRST" + del + s)
            .assertAsSuggester(new EnumArgumentResolver<>(), TEST_ENUM_ARGUMENT, s -> "FIRST" + del + s, "");

        platform.suggest(command + " enum FIRST")
            .assertSuggest("FIRST" + del, "FIRST");
    }

}