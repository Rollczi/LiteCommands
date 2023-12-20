package dev.rollczi.litecommands.annotations.argument.resolver.standard;

import dev.rollczi.litecommands.annotations.LiteTestSpec;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class ArrayArgumentTest extends LiteTestSpec {

    enum TestEnum {
        FIRST,
        SECOND,
        THIRD,
        FOURTH
    }

    @Command(name = "test")
    static class TestCommand {

        @Execute(name = "int")
        int[] test(@Arg int[] test) {
            return test;
        }

        @Execute(name = "Integer")
        Integer[] test(@Arg Integer[] test) {
            return test;
        }

        @Execute(name = "String")
        String[] test(@Arg("argument") String[] test) {
            return test;
        }

        @Execute(name = "Duration")
        Duration[] test(@Arg Duration[] test) {
            return test;
        }

        @Execute(name = "Instant")
        Instant[] test(@Arg Instant[] test) {
            return test;
        }

        @Execute(name = "enum")
        TestEnum[] test(@Arg TestEnum[] test) {
            return test;
        }
    }

    @Test
    void test() {
        platform.execute("test int 1 2 3 4 5")
            .assertSuccess(new int[]{1, 2, 3, 4, 5});

        platform.execute("test Integer 1 2 3 4 5")
            .assertSuccess(new Integer[]{1, 2, 3, 4, 5});

        platform.execute("test String text1 text2 text3 text4 text5")
            .assertSuccess(new String[]{"text1", "text2", "text3", "text4", "text5"});

        platform.execute("test Duration 10m 1h2m3s 1h 1d3h 2d")
            .assertSuccess(new Duration[]{
                Duration.ofMinutes(10),
                Duration.ofHours(1).plusMinutes(2).plusSeconds(3),
                Duration.ofHours(1),
                Duration.ofDays(1).plusHours(3),
                Duration.ofDays(2)
            });

        platform.execute("test Instant 2021-01-01 00:05:50 2023-04-17 11:03:00 2016-12-31 23:59:59")
            .assertSuccess(new Instant[]{
                Instant.parse("2021-01-01T00:05:50Z"),
                Instant.parse("2023-04-17T11:03:00Z"),
                Instant.parse("2016-12-31T23:59:59Z")
            });

        platform.execute("test enum FIRST SECOND THIRD FOURTH")
            .assertSuccess(new TestEnum[]{
                TestEnum.FIRST,
                TestEnum.SECOND,
                TestEnum.THIRD,
                TestEnum.FOURTH
            });
    }

    @Test
    void testSingle() {
        platform.execute("test int 1")
            .assertSuccess(new int[]{1});

        platform.execute("test Integer 1")
            .assertSuccess(new Integer[]{1});

        platform.execute("test String text")
            .assertSuccess(new String[]{"text"});

        platform.execute("test Duration 1h")
            .assertSuccess(new Duration[]{Duration.ofHours(1)});

        platform.execute("test Instant 2021-01-01 00:05:50")
            .assertSuccess(new Instant[]{Instant.parse("2021-01-01T00:05:50Z")});

        platform.execute("test enum FIRST")
            .assertSuccess(new TestEnum[]{TestEnum.FIRST});
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
        platform.execute("test int")
            .assertSuccess(new int[]{});

        platform.execute("test int ")
            .assertFailure();

        platform.execute("test Integer")
            .assertSuccess(new Integer[]{});

        platform.execute("test Integer ")
            .assertFailure();

        platform.execute("test String")
            .assertSuccess(new String[]{});

        platform.execute("test String ")
            .assertSuccess(new String[]{""});

        platform.execute("test Duration")
            .assertSuccess(new Duration[]{});

        platform.execute("test Duration ")
            .assertFailure();

        platform.execute("test Instant")
            .assertSuccess(new Instant[]{});

        platform.execute("test Instant ")
            .assertFailure();

        platform.execute("test enum")
            .assertSuccess(new TestEnum[]{});

        platform.execute("test enum ")
            .assertFailure();
    }

    @Test
    void testSuggestInt() {
        platform.suggest("test int ")
            .assertSuggest("0", "1", "5", "10", "50", "100", "500");
        platform.suggest("test int 1 ")
            .assertSuggest("0", "1", "5", "10", "50", "100", "500");
        platform.suggest("test int 5")
            .assertSuggest("5", "500", "50.0", "50");
    }

    @Test
    void testSuggestInteger() {
        platform.suggest("test Integer ")
            .assertSuggest("0", "1", "5", "10", "50", "100", "500");
        platform.suggest("test Integer 1 ")
            .assertSuggest("0", "1", "5", "10", "50", "100", "500");
        platform.suggest("test Integer 5")
            .assertSuggest("5", "500", "50.0", "50");
    }

    @Test
    void testSuggestString() {
        platform.suggest("test String ")
            .assertSuggest("<argument>");
        platform.suggest("test String <argument>")
            .assertSuggest("<argument>");
        platform.suggest("test String text ")
            .assertSuggest("<argument>");
        platform.suggest("test String text")
            .assertSuggest("text");
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
        String tomorrowFormat = tomorrow.getYear() + "-" + tomorrow.getMonthValue() + "-" + tomorrow.getDayOfMonth();

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
            .assertSuggest("FIRST");
    }


}