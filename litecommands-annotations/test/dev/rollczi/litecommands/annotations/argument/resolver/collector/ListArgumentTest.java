
package dev.rollczi.litecommands.annotations.argument.resolver.collector;

import dev.rollczi.litecommands.annotations.LiteTestSpec;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.argument.resolver.standard.NumberArgumentResolver;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import static java.util.Collections.*;
import java.util.List;
import org.junit.jupiter.api.Test;

class ListArgumentTest extends LiteTestSpec {

    private final static DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    enum TestEnum {
        FIRST,
        SECOND,
        THIRD,
        FOURTH
    }

    @Command(name = "test")
    static class TestCommand {

        @Execute(name = "Integer")
        List<Integer> testInteger(@Arg List<Integer> test) {
            return test;
        }

        @Execute(name = "String")
        List<String> testString(@Arg("argument") List<String> test) {
            return test;
        }

        @Execute(name = "Duration")
        List<Duration> testDuration(@Arg List<Duration> test) {
            return test;
        }

        @Execute(name = "Instant")
        List<Instant> testInstant(@Arg List<Instant> test) {
            return test;
        }

        @Execute(name = "enum")
        List<TestEnum> testTestEnum(@Arg List<TestEnum> test) {
            return test;
        }
    }

    @Test
    void test() {
        platform.execute("test Integer 1 2 3 4 5")
            .assertSuccess(Arrays.asList(1, 2, 3, 4, 5));

        platform.execute("test String text1 text2 text3 text4 text5")
            .assertSuccess(Arrays.asList("text1", "text2", "text3", "text4", "text5"));

        platform.execute("test Duration 10m 1h2m3s 1h 1d3h 2d")
            .assertSuccess(Arrays.asList(
                Duration.ofMinutes(10),
                Duration.ofHours(1).plusMinutes(2).plusSeconds(3),
                Duration.ofHours(1),
                Duration.ofDays(1).plusHours(3),
                Duration.ofDays(2)
            ));

        platform.execute("test Instant 2021-01-01 00:05:50 2023-04-17 11:03:00 2016-12-31 23:59:59")
            .assertSuccess(Arrays.asList(
                Instant.parse("2021-01-01T00:05:50Z"),
                Instant.parse("2023-04-17T11:03:00Z"),
                Instant.parse("2016-12-31T23:59:59Z")
            ));

        platform.execute("test enum FIRST SECOND THIRD FOURTH")
            .assertSuccess(Arrays.asList(
                TestEnum.FIRST,
                TestEnum.SECOND,
                TestEnum.THIRD,
                TestEnum.FOURTH
            ));
    }

    @Test
    void testSingle() {
        platform.execute("test Integer 1")
            .assertSuccess(Arrays.asList(1));

        platform.execute("test String text")
            .assertSuccess(Arrays.asList("text"));

        platform.execute("test Duration 1h")
            .assertSuccess(Arrays.asList(Duration.ofHours(1)));

        platform.execute("test Instant 2021-01-01 00:05:50")
            .assertSuccess(Arrays.asList(Instant.parse("2021-01-01T00:05:50Z")));

        platform.execute("test enum FIRST")
            .assertSuccess(Arrays.asList(TestEnum.FIRST));
    }

    @Test
    void testInvalid() {
        platform.execute("test int 1acab")
            .assertFailure();

        platform.execute("test int 1 a b")
            .assertFailure();

        platform.execute("test Integer 1acab")
            .assertFailure();

        platform.execute("test Integer 1 a b")
            .assertFailure();

        platform.execute("test Duration 10m 1h2m3s Invalid 1d3h 2d 1h")
            .assertFailure();

        platform.execute("test Duration 10m 1h2m3s 3 1d3h 2d 1h")
            .assertFailure();

        platform.execute("test Instant 2021-01-01 Invalid")
            .assertFailure();

        platform.execute("test Instant 2021-01-01 00:05:50 2023-04-17 Invalid 2016-12-31 23:59:59")
            .assertFailure();

        platform.execute("test Instant Invalid 00:05:50 2023-04-17 00:05:50 2016-12-31 23:59:59")
            .assertFailure();

        platform.execute("test enum FIRST SECOND THIRD FOURTH Invalid")
            .assertFailure();

        platform.execute("test enum FIRST SECOND THIRD FOURTH 1")
            .assertFailure();
    }


    @Test
    void testEmpty() {
        platform.execute("test Integer")
            .assertSuccess(emptyList());

        platform.execute("test Integer ")
            .assertSuccess(emptyList());

        platform.execute("test String")
            .assertSuccess(emptyList());

        platform.execute("test String ")
            .assertSuccess(singletonList(""));

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
        platform.suggest("test Integer 1 ")
            .assertAsSuggester(NumberArgumentResolver.ofInteger(), "");
        platform.suggest("test Integer 5")
            .assertSuggestAndFlush("5 ", "5")
            .assertAsSuggester(NumberArgumentResolver.ofInteger(), "5");
    }

    @Test
    void testSuggestString() {
        platform.suggest("test String ")
            .assertSuggest("<argument>");
        platform.suggest("test String <argument>")
            .assertSuggest("<argument>", "<argument> ");
        platform.suggest("test String text ")
            .assertSuggest("<argument>");
        platform.suggest("test String text")
            .assertSuggest("text", "text ");
    }

    @Test
    void testSuggestDuration() {
        platform.suggest("test Duration ")
            .assertSuggest("1s", "1d", "1h", "1m", "30d", "10h", "7d", "10m", "30m", "5h", "10s", "30s", "5m", "5s", "1m30s");
        platform.suggest("test Duration 10m ")
            .assertSuggest("1s", "1d", "1h", "1m", "30d", "10h", "7d", "10m", "30m", "5h", "10s", "30s", "5m", "5s", "1m30s");
        platform.suggest("test Duration 10")
            .assertSuggest("10s", "10m", "10h");
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
        platform.suggest("test Instant 2021-01-01 00:05:50 2023-04-17 11:03:00 " + tomorrowFormat + " ")
            .assertNotEmpty().assertCorrect(suggestion -> platform.execute("test Instant " + tomorrowFormat + " " + suggestion.multilevel()).assertSuccess());
    }

    @Test
    void testSuggestEnum() {
        platform.suggest("test enum ")
            .assertSuggest("FIRST", "SECOND", "THIRD", "FOURTH");
        platform.suggest("test enum FIRST ")
            .assertSuggest("FIRST", "SECOND", "THIRD", "FOURTH");
        platform.suggest("test enum FIRST")
            .assertSuggest("FIRST", "FIRST ");
    }


}