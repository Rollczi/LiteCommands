
package dev.rollczi.litecommands.annotations.argument.resolver.collector;

import dev.rollczi.litecommands.annotations.LiteTestSpec;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.varargs.Varargs;
import dev.rollczi.litecommands.argument.resolver.standard.NumberArgumentResolver;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import static java.util.Arrays.asList;
import static java.util.Collections.*;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import panda.std.Pair;

class VarargsArgumentTest extends LiteTestSpec {

    private final static DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static final String COMMAND = "test";

    enum TestEnum {
        FIRST,
        SECOND,
        THIRD,
        FOURTH
    }

    @Command(name = COMMAND)
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
    }

    @Test
    void test() {
        platform.execute("test Integer 1,2,3,4,5")
            .assertSuccess(asList(1, 2, 3, 4, 5));

        platform.execute("test String text1,text2,text3,text4,text5")
            .assertSuccess(asList("text1", "text2", "text3", "text4", "text5"));

        platform.execute("test Duration 10m,1h2m3s,1h,1d3h,2d")
            .assertSuccess(asList(
                Duration.ofMinutes(10),
                Duration.ofHours(1).plusMinutes(2).plusSeconds(3),
                Duration.ofHours(1),
                Duration.ofDays(1).plusHours(3),
                Duration.ofDays(2)
            ));

        platform.execute("test Instant 2021-01-01 00:05:50,2023-04-17 11:03:00,2016-12-31 23:59:59")
            .assertSuccess(asList(
                Instant.parse("2021-01-01T00:05:50Z"),
                Instant.parse("2023-04-17T11:03:00Z"),
                Instant.parse("2016-12-31T23:59:59Z")
            ));

        platform.execute("test Instant-special 2021-01-01 00:05:50,2023-04-17 11:03:00,2016-12-31 23:59:59 5")
            .assertSuccess(Pair.of(asList(
                Instant.parse("2021-01-01T00:05:50Z"),
                Instant.parse("2023-04-17T11:03:00Z"),
                Instant.parse("2016-12-31T23:59:59Z")
            ), 5));

        platform.execute("test enum FIRST,SECOND,THIRD,FOURTH")
            .assertSuccess(asList(
                TestEnum.FIRST,
                TestEnum.SECOND,
                TestEnum.THIRD,
                TestEnum.FOURTH
            ));
    }

    @Test
    void testSingle() {
        platform.execute("test Integer 1")
            .assertSuccess(asList(1));

        platform.execute("test String text")
            .assertSuccess(asList("text"));

        platform.execute("test Duration 1h")
            .assertSuccess(asList(Duration.ofHours(1)));

        platform.execute("test Instant 2021-01-01 00:05:50")
            .assertSuccess(asList(Instant.parse("2021-01-01T00:05:50Z")));

        platform.execute("test enum FIRST")
            .assertSuccess(asList(TestEnum.FIRST));
    }

    @Test
    void testInvalid() {
        platform.execute("test Integer 1acab")
            .assertFailure();

        platform.execute("test Integer 1,a,b")
            .assertFailure();

        platform.execute("test Duration 10m,1h2m3s,Invalid,1d3h,2d,1h")
            .assertFailure();

        platform.execute("test Duration 10m,1h2m3s,3,1d3h,2d,1h")
            .assertFailure();

        platform.execute("test Instant 2021-01-01 Invalid")
            .assertFailure();

        platform.execute("test Instant 2021-01-01 00:05:50,2023-04-17 Invalid,2016-12-31 23:59:59")
            .assertFailure();

        platform.execute("test Instant Invalid 00:05:50,2023-04-17 00:05:50,2016-12-31 23:59:59")
            .assertFailure();

        platform.execute("test enum FIRST,SECOND,THIRD,FOURTH,Invalid")
            .assertFailure();

        platform.execute("test enum FIRST,SECOND,THIRD,FOURTH,1")
            .assertFailure();

        platform.execute("test Instant 2021-01-01 00:05:50,2023-04-17 11:03:00,2016-12-31 ")
            .assertFailure();

        platform.execute("test Instant 2021-01-01 00:05:50,2023-04-17 11:03:00,")
            .assertFailure();
    }


    @Test
    void testEmpty() {
        platform.execute("test Integer")
            .assertSuccess(asList());

        platform.execute("test Integer ")
            .assertSuccess(emptyList());

        platform.execute("test String")
            .assertSuccess(emptyList());

        platform.execute("test String ")
            .assertSuccess(emptyList());

        platform.execute("test Duration")
            .assertSuccess(emptyList());

        platform.execute("test Duration ")
            .assertSuccess(emptyList());

        platform.execute("test Instant")
            .assertSuccess(emptyList());

        platform.execute("test Instant ")
            .assertSuccess(emptyList());

        platform.execute("test enum")
            .assertSuccess(emptyList());

        platform.execute("test enum ")
            .assertSuccess(emptyList());
    }

    @Test
    void testSuggestInteger() {
        platform.suggest("test Integer ")
            .assertAsSuggester(NumberArgumentResolver.ofInteger(), "");
        platform.suggest("test Integer 1,")
            .assertAsSuggester(NumberArgumentResolver.ofInteger(), suggest -> "1," + suggest, "");
        platform.suggest("test Integer 5")
            .assertSuggestAndFlush("5,", "5")
            .assertAsSuggester(NumberArgumentResolver.ofInteger(), "5");
    }

    @Test
    void testSuggestString() {
        platform.suggest("test String ")
            .assertSuggest("<argument>");
        platform.suggest("test String <argument>")
            .assertSuggest("<argument>", "<argument>,");
        platform.suggest("test String text,")
            .assertSuggest("text,", "text,<argument>");
        platform.suggest("test String text")
            .assertSuggest("text", "text,");
    }

    @ParameterizedTest
    @MethodSource("provideDurationArgs")
    void testSuggestDuration(String input, List<String> expectedSuggestions) {
        platform.suggest(input).assertSuggest(expectedSuggestions.toArray(new String[0]));
    }

    private static Stream<Arguments> provideDurationArgs() {
        return Stream.of(
            Arguments.of("test Duration ", asList("1s", "1d", "1h", "1m", "30d", "10h", "7d", "10m", "30m", "5h", "10s", "30s", "5m", "5s", "1m30s")),
            Arguments.of("test Duration 10", asList("10s", "10m", "10h")),
            Arguments.of("test Duration 1m", asList( "1m", "1m,", "1m30s")),
            Arguments.of("test Duration 10h,", asList("10h,1s", "10h,1m", "10h,1d", "10h,1h", "10h,30d", "10h,10h", "10h,7d", "10h,10m", "10h,30m", "10h,5h", "10h,10s", "10h,30s", "10h,5m", "10h,5s", "10h,1m30s")),
            Arguments.of("test Duration 10h,1", asList("10h,1s", "10h,1m", "10h,1d", "10h,1h", "10h,10h", "10h,10m", "10h,10s", "10h,1m30s")),
            Arguments.of("test Duration 10h,1m", asList("10h,1m", "10h,1m,", "10h,1m30s")),
            Arguments.of("test Duration 10h,1m,", asList("10h,1m,1s", "10h,1m,1m", "10h,1m,1d", "10h,1m,1h", "10h,1m,30d", "10h,1m,10h", "10h,1m,7d", "10h,1m,10m", "10h,1m,30m", "10h,1m,5h", "10h,1m,10s", "10h,1m,30s", "10h,1m,5m", "10h,1m,5s", "10h,1m,1m30s")),
            Arguments.of("test Duration 10h,1m,1", asList("10h,1m,1s", "10h,1m,10s", "10h,1m,1m", "10h,1m,1d", "10h,1m,1h", "10h,1m,10h", "10h,1m,10m", "10h,1m,1m30s")),
            Arguments.of("test Duration 10h,1m,2m", asList("10h,1m,2m", "10h,1m,2m,"))
        );
    }

    @Test
    void testSuggestInstantSpecial() {
        platform.suggest("test Instant-special ")
            .assertNotEmpty()
            .assertCorrect(suggestion -> {
                String multilevel = suggestion.multilevel();

                if (multilevel.equals("-")) {
                    return;
                }

                if (multilevel.chars().allMatch(codePoint -> Character.isDigit(codePoint))) {
                    platform.execute("test Instant-special " + multilevel).assertSuccess();
                    return;
                }

                platform.execute("test Instant-special " + multilevel + " 5").assertSuccess();
            });
    }


    @Test
    void testSuggestInstant() {
        platform.suggest("test Instant ")
            .assertNotEmpty().assertCorrect(suggestion -> platform.execute("test Instant " + suggestion.multilevel()).assertSuccess());

        LocalDate tomorrow = LocalDate.now().plusDays(1);
        String tomorrowFormat = DATE_FORMAT.format(tomorrow);

        platform.suggest("test Instant " + tomorrow.getYear())
            .assertNotEmpty().assertCorrect(suggestion -> platform.execute("test Instant " + suggestion.multilevel()).assertSuccess());
        platform.suggest("test Instant " + tomorrowFormat + " ")
            .assertNotEmpty().assertCorrect(suggestion -> platform.execute("test Instant " + tomorrowFormat + " " + suggestion.multilevel()).assertSuccess());
        platform.suggest("test Instant 2021-01-01 00:05:50,2023-04-17 11:03:00," + tomorrowFormat + " ")
            .assertNotEmpty().assertCorrect(suggestion -> platform.execute("test Instant " + tomorrowFormat + " " + suggestion.multilevel()).assertSuccess());
    }

    @Test
    void testSuggestEnum() {
        platform.suggest("test enum ")
            .assertSuggest("FIRST", "SECOND", "THIRD", "FOURTH");
        platform.suggest("test enum FIRST,")
            .assertSuggest("FIRST,FIRST", "FIRST,SECOND", "FIRST,THIRD", "FIRST,FOURTH");
        platform.suggest("test enum FIRST")
            .assertSuggest("FIRST,", "FIRST");
    }


}