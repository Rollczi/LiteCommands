package dev.rollczi.litecommands.annotations.argument.resolver.collector

import dev.rollczi.litecommands.annotations.LiteTestSpec
import dev.rollczi.litecommands.annotations.argument.Arg
import dev.rollczi.litecommands.annotations.command.Command
import dev.rollczi.litecommands.annotations.execute.Execute
import dev.rollczi.litecommands.annotations.varargs.Varargs
import dev.rollczi.litecommands.argument.Argument
import dev.rollczi.litecommands.argument.resolver.standard.DurationArgumentResolver
import dev.rollczi.litecommands.argument.resolver.standard.EnumArgumentResolver
import dev.rollczi.litecommands.argument.resolver.standard.NumberArgumentResolver
import dev.rollczi.litecommands.invalidusage.InvalidUsage.Cause
import dev.rollczi.litecommands.suggestion.Suggestion
import dev.rollczi.litecommands.unit.AssertExecute
import dev.rollczi.litecommands.wrapper.WrapFormat
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import panda.std.Pair
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Optional
import java.util.stream.Stream

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class VarargsArgumentTest : LiteTestSpec() {

    private val dateFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        .withZone(ZoneOffset.UTC)

    companion object {
        @JvmStatic
        private fun delimiters(): Stream<Arguments> {
            return Stream.of(
                Arguments.of("test normal", ","),
                Arguments.of("test with-space", ", "),
                Arguments.of("test custom", " | ")
            )
        }
    }

    val TEST_ENUM_ARGUMENT = Argument.of("test", WrapFormat.notWrapped(TestEnum::class.java))

    enum class TestEnum {
        FIRST,
        SECOND,
        THIRD,
        FOURTH
    }

    @Command(name = "test normal")
    class TestCommand {
        @Execute(name = "Integer")
        fun testInteger(@Varargs(delimiter = ",") test: List<Int>): List<Int> {
            return test
        }

        @Execute(name = "String")
        fun testString(@Varargs(value = "argument", delimiter = ",") test: List<String>): List<String> {
            return test
        }

        @Execute(name = "Duration")
        fun testDuration(@Varargs(delimiter = ",") test: List<Duration>): List<Duration> {
            return test
        }

        @Execute(name = "Instant")
        fun testInstant(@Varargs(delimiter = ",") test: List<Instant>): List<Instant> {
            return test
        }


        @Execute(name = "Instant-special")
        fun testInstantSpecial(@Varargs(delimiter = ",") test: List<Instant>, @Arg a: Int): Pair<List<Instant>, Int> {
            return Pair.of(test, a)
        }

        @Execute(name = "enum")
        fun testTestEnum(@Varargs(delimiter = ",") test: List<TestEnum>): List<TestEnum> {
            return test
        }

        @Execute(name = "durations-instant")
        fun testInstantDuration(@Varargs(delimiter = ",") durations: List<Duration>, @Arg instant: Instant): Pair<List<Duration>, Instant> {
            return Pair.of(durations, instant)
        }
    }

    @Command(name = "test with-space")
    class TestWithSpaceCommand {
        @Execute(name = "Integer")
        fun testInteger(@Varargs(delimiter = ", ") test: List<Int>): List<Int> {
            return test
        }

        @Execute(name = "String")
        fun testString(@Varargs(value = "argument", delimiter = ", ") test: List<String>): List<String> {
            return test
        }

        @Execute(name = "Duration")
        fun testDuration(@Varargs(delimiter = ", ") test: List<Duration>): List<Duration> {
            return test
        }

        @Execute(name = "Instant")
        fun testInstant(@Varargs(delimiter = ", ") test: List<Instant>): List<Instant> {
            return test
        }


        @Execute(name = "Instant-special")
        fun testInstantSpecial(@Varargs(delimiter = ", ") test: List<Instant>, @Arg a: Int): Pair<List<Instant>, Int> {
            return Pair.of(test, a)
        }

        @Execute(name = "enum")
        fun testTestEnum(@Varargs(delimiter = ", ") test: List<TestEnum>): List<TestEnum> {
            return test
        }

        @Execute(name = "durations-instant")
        fun testInstantDuration(@Varargs(delimiter = ", ") durations: List<Duration>, @Arg instant: Instant): Pair<List<Duration>, Instant> {
            return Pair.of(durations, instant)
        }
    }

    @Command(name = "test custom")
    class TestCustomCommand {
        @Execute(name = "Integer")
        fun testInteger(@Varargs(delimiter = " | ") test: List<Int>): List<Int> {
            return test
        }

        @Execute(name = "String")
        fun testString(@Varargs(value = "argument", delimiter = " | ") test: List<String>): List<String> {
            return test
        }

        @Execute(name = "Duration")
        fun testDuration(@Varargs(delimiter = " | ") test: List<Duration>): List<Duration> {
            return test
        }

        @Execute(name = "Instant")
        fun testInstant(@Varargs(delimiter = " | ") test: List<Instant>): List<Instant> {
            return test
        }


        @Execute(name = "Instant-special")
        fun testInstantSpecial(@Varargs(delimiter = " | ") test: List<Instant>, @Arg a: Int): Pair<List<Instant>, Int> {
            return Pair.of(test, a)
        }

        @Execute(name = "enum")
        fun testTestEnum(@Varargs(delimiter = " | ") test: List<TestEnum>): List<TestEnum> {
            return test
        }

        @Execute(name = "durations-instant")
        fun testInstantDuration(@Varargs(delimiter = " | ") durations: List<Duration>, @Arg instant: Instant): Pair<List<Duration>, Instant> {
            return Pair.of(durations, instant)
        }
    }

    @ParameterizedTest
    @MethodSource("delimiters")
    @DisplayName("Test parsing multiple arguments with different delimiters")
    fun testParsing(command: String, del: String) {
        platform.execute("$command Integer 1${del}2${del}3${del}4${del}5")
            .assertSuccess(listOf(1, 2, 3, 4, 5))

        platform.execute("$command String text1${del}text2${del}text3${del}text4${del}text5")
            .assertSuccess(listOf("text1", "text2", "text3", "text4", "text5"))

        platform.execute("$command Duration 10m${del}1h2m3s${del}1h${del}1d3h${del}2d")
            .assertSuccess(listOf(
                Duration.ofMinutes(10),
                Duration.ofHours(1).plusMinutes(2).plusSeconds(3),
                Duration.ofHours(1),
                Duration.ofDays(1).plusHours(3),
                Duration.ofDays(2)
            ))

        platform.execute("$command Instant 2021-01-01 00:05:50${del}2023-04-17 11:03:00${del}2016-12-31 23:59:59")
            .assertSuccess(listOf(
                Instant.parse("2021-01-01T00:05:50Z"),
                Instant.parse("2023-04-17T11:03:00Z"),
                Instant.parse("2016-12-31T23:59:59Z")
            ))

        platform.execute("$command Instant-special 2021-01-01 00:05:50${del}2023-04-17 11:03:00${del}2016-12-31 23:59:59 5")
            .assertSuccess(Pair.of(listOf(
                Instant.parse("2021-01-01T00:05:50Z"),
                Instant.parse("2023-04-17T11:03:00Z"),
                Instant.parse("2016-12-31T23:59:59Z")
            ), 5))

        platform.execute("$command enum FIRST${del}SECOND${del}THIRD${del}FOURTH")
            .assertSuccess(listOf(
                TestEnum.FIRST,
                TestEnum.SECOND,
                TestEnum.THIRD,
                TestEnum.FOURTH
            ))
    }

    @ParameterizedTest
    @MethodSource("delimiters")
    @DisplayName("Test parsing single argument with different delimiters")
    fun testParsingSingle(command: String, delimiter: String) {
        platform.execute("$command Integer 1")
            .assertSuccess(listOf(1))

        platform.execute("$command String text")
            .assertSuccess(listOf("text"))

        platform.execute("$command Duration 1h")
            .assertSuccess(listOf(Duration.ofHours(1)))

        platform.execute("$command Instant 2021-01-01 00:05:50")
            .assertSuccess(listOf(Instant.parse("2021-01-01T00:05:50Z")))

        platform.execute("$command enum FIRST")
            .assertSuccess(listOf(TestEnum.FIRST))
    }

    @ParameterizedTest
    @MethodSource("delimiters")
    @DisplayName("Test parsing invalid arguments with different delimiters")
    fun testParsingInvalidArguments(command: String, del: String) {
        platform.execute("$command Integer 1acab")
            .assertFailureInvalid(Cause.INVALID_ARGUMENT)

        platform.execute("$command Integer 1${del}a${del}b")
            .assertFailureInvalid(Cause.INVALID_ARGUMENT)

        platform.execute("$command Duration 10m${del}1h2m3s${del}Invalid${del}1d3h${del}2d${del}1h")
            .assertFailureInvalid(Cause.INVALID_ARGUMENT)

        platform.execute("$command Duration 10m${del}1h2m3s${del}3${del}1d3h${del}2d${del}1h")
            .assertFailureInvalid(Cause.INVALID_ARGUMENT)

        platform.execute("$command enum FIRST${del}SECOND${del}THIRD${del}FOURTH${del}Invalid")
            .assertFailureInvalid(Cause.INVALID_ARGUMENT)

        platform.execute("$command enum FIRST${del}SECOND${del}THIRD${del}FOURTH${del}1")
            .assertFailureInvalid(Cause.INVALID_ARGUMENT)

        platform.execute("$command Instant 2021-01-01 Invalid")
            .assertOptionalFailure("Invalid date format '2021-01-01 Invalid'! Use: <yyyy-MM-dd> <HH:mm:ss> (INSTANT_INVALID_FORMAT)")

        platform.execute("$command Instant 2021-01-01 00:05:50${del}2023-04-17 Invalid${del}2016-12-31 23:59:59")
            .assertOptionalFailure("Invalid date format '2023-04-17 Invalid'! Use: <yyyy-MM-dd> <HH:mm:ss> (INSTANT_INVALID_FORMAT)")

         platform.execute("$command Instant Invalid 00:05:50${del}2023-04-17 00:05:50${del}2016-12-31 23:59:59")
            .assertOptionalFailure("Invalid date format 'Invalid 00:05:50'! Use: <yyyy-MM-dd> <HH:mm:ss> (INSTANT_INVALID_FORMAT)")

        platform.execute("$command Instant 2021-01-01 00:05:50${del}2023-04-17 11:03:00${del}2016-12-31 ")
            .assertOptionalFailure("Invalid date format '2016-12-31 '! Use: <yyyy-MM-dd> <HH:mm:ss> (INSTANT_INVALID_FORMAT)")

        platform.execute("$command Instant 2021-01-01 00:05:50${del}2023-04-17 11:03:00${del}")
            .assertFailureInvalid(Cause.MISSING_PART_OF_ARGUMENT)
    }

    private fun AssertExecute.assertOptionalFailure(message: String) {
        val optional = assertFailedAs(Optional::class.java)
            .map { it as String }

        assertThat(optional)
            .isPresent
            .hasValue(message)
    }

    @ParameterizedTest
    @MethodSource("delimiters")
    @DisplayName("Test parsing empty arguments with different delimiters")
    fun testEmpty(command: String, delimiter: String) {
        platform.execute("$command Integer")
            .assertSuccess(listOf<Any>())

        platform.execute("$command Integer ")
            .assertSuccess(emptyList<Any>())

        platform.execute("$command String")
            .assertSuccess(emptyList<Any>())

        platform.execute("$command String ")
            .assertSuccess(emptyList<Any>())

        platform.execute("$command Duration")
            .assertSuccess(emptyList<Any>())

        platform.execute("$command Duration ")
            .assertSuccess(emptyList<Any>())

        platform.execute("$command Instant")
            .assertSuccess(emptyList<Any>())

        platform.execute("$command Instant ")
            .assertSuccess(emptyList<Any>())

        platform.execute("$command enum")
            .assertSuccess(emptyList<Any>())

        platform.execute("$command enum ")
            .assertSuccess(emptyList<Any>())
    }

    @ParameterizedTest
    @MethodSource("delimiters")
    @DisplayName("Test suggest integer with different delimiters")
    fun testSuggestInteger(command: String, delimiter: String) {
        platform.suggest("$command Integer ")
            .assertAsSuggester(NumberArgumentResolver.ofInteger(), "")

        platform.suggest("$command Integer 1$delimiter")
            .mapIf(delimiter.contains(" ")) { "1$delimiter$it" } // if delimiter contains space, add the rest of the argument
            .assertAsSuggester(NumberArgumentResolver.ofInteger(), { "1$delimiter$it" }, "")

        platform.suggest("$command Integer 5")
            .assertSuggestAndFlush("5$delimiter", "5")
            .assertAsSuggester(NumberArgumentResolver.ofInteger(), "5")
    }

    @ParameterizedTest
    @MethodSource("delimiters")
    @DisplayName("Test suggest string with different delimiters")
    fun testSuggestString(command: String, delimiter: String) {
        platform.suggest("$command String ")
            .assertSuggest("<argument>")
        platform.suggest("$command String <argument>")
            .assertSuggest("<argument>", "<argument>$delimiter")
        platform.suggest("$command String text$delimiter")
            .mapIf(delimiter.contains(" ")) { "text$delimiter$it" }
            .assertSuggest("text$delimiter", "text$delimiter<argument>")

        platform.suggest("$command String text")
            .assertSuggest("text", "text$delimiter")
    }

    @ParameterizedTest
    @MethodSource("delimiters")
    @DisplayName("Test suggest Duration with different delimiters")
    fun testSuggestDuration(command: String, del: String) {
        // ''
        platform.suggest("$command Duration ")
            .assertAsSuggester(DurationArgumentResolver(), "")

        // '10'
        platform.suggest("$command Duration 10")
            .assertAsSuggester(DurationArgumentResolver(), "10")

        // '1m'
        platform.suggest("$command Duration 1m")
            .assertSuggestAndFlush("1m$del")
            .assertAsSuggester(DurationArgumentResolver(), "1m")

        // '10h,'
        platform.suggest("$command Duration 10h$del")
            .mapIf(del.contains(" ")) { "10h$del$it" }
            .assertAsSuggester(DurationArgumentResolver(), { "10h$del$it" }, "")

        // '10h,1'
        platform.suggest("$command Duration 10h${del}1")
            .mapIf(del.contains(" ")) { "10h$del$it" }
            .assertAsSuggester(DurationArgumentResolver(), { "10h$del$it" }, "1")

        // '10h,1m'
        platform.suggest("$command Duration 10h${del}1m")
            .mapIf(del.contains(" ")) { "10h${del}$it" }
            .assertSuggestAndFlush("10h${del}1m$del")
            .assertAsSuggester(DurationArgumentResolver(), { "10h$del$it" }, "1m")

        // '10h,1m,'
        platform.suggest("$command Duration 10h${del}1m$del")
            .mapIf(del.contains(" ")) { "10h${del}1m$del$it" }
            .assertAsSuggester(DurationArgumentResolver(), { "10h${del}1m${del}$it" }, "")

        // '10h,1m,1'
        platform.suggest("$command Duration 10h${del}1m${del}1")
            .mapIf(del.contains(" ")) { "10h${del}1m$del$it" }
            .assertAsSuggester(DurationArgumentResolver(), { "10h${del}1m$del$it" }, "1")

        // '10h,1m,2m'
        platform.suggest("$command Duration 10h${del}1m${del}5m")
            .mapIf(del.contains(" ")) { "10h${del}1m${del}$it" }
            .assertSuggestAndFlush("10h${del}1m${del}5m$del")
            .assertAsSuggester(DurationArgumentResolver(), { "10h${del}1m${del}$it" }, "5m")
    }

    @ParameterizedTest
    @MethodSource("delimiters")
    @DisplayName("Test suggest Instant list with different delimiters with additional int argument at the end of the command")
    fun testSuggestInstantSpecial(command: String, del: String) {
        platform.suggest("$command Instant-special ")
            .assertNotEmpty()
            .assertCorrect { suggestion: Suggestion ->
                val multilevel = suggestion.multilevel()
                if (multilevel == "-") {
                    return@assertCorrect
                }

                if (multilevel.chars().allMatch { codePoint: Int -> Character.isDigit(codePoint) }) {
                    platform.execute("$command Instant-special $multilevel").assertSuccess()
                    return@assertCorrect
                }
                platform.execute("$command Instant-special $multilevel 5").assertSuccess()
            }
    }


    @ParameterizedTest
    @MethodSource("delimiters")
    @DisplayName("Test suggest Duration list with different delimiters with additional Instant argument at the end of the command")
    fun testSuggestDurationInstant(command: String, del: String) {
        platform.suggest("$command durations-instant ")
            .assertNotEmpty()
            .assertSuggestAndFlush(DurationArgumentResolver.SUGGESTIONS_LIST)
            .assertCorrect { suggestion: Suggestion ->
                val multilevel = suggestion.multilevel()
                if (multilevel == "-") {
                    return@assertCorrect
                }

                val instant = Instant.from(dateTimeFormatter.parse(multilevel))

                platform.execute("$command durations-instant $multilevel")
                    .assertSuccess(Pair.of(listOf<Duration>(), instant))

                platform.execute("$command durations-instant 10s $multilevel")
                    .assertSuccess(Pair.of(listOf(Duration.ofSeconds(10)), instant))

                platform.execute("$command durations-instant 10s${del}31m $multilevel")
                    .assertSuccess(Pair.of(listOf(Duration.ofSeconds(10), Duration.ofMinutes(31)), instant))
            }
    }


    @ParameterizedTest
    @MethodSource("delimiters")
    @DisplayName("Test suggest Instant with different delimiters")
    fun testSuggestInstant(command: String, del: String) {
        platform.suggest("$command Instant ")
            .assertNotEmpty()
            .assertCorrect { platform.execute("$command Instant " + it.multilevel()).assertSuccess() }

        val tomorrow = LocalDate.now().plusDays(1)
        val tomorrowFormat = dateFormat.format(tomorrow)

        platform.suggest("$command Instant " + tomorrow.year)
            .assertNotEmpty()
            .assertCorrect { platform.execute("$command Instant " + it.multilevel()).assertSuccess() }

        platform.suggest("$command Instant $tomorrowFormat ")
            .assertNotEmpty()
            .assertCorrect { platform.execute("$command Instant " + tomorrowFormat + " " + it.multilevel()).assertSuccess() }

        platform.suggest("$command Instant 2021-01-01 00:05:50${del}2023-04-17 11:03:00${del}$tomorrowFormat ")
            .assertNotEmpty()
            .assertCorrect { platform.execute("$command Instant " + tomorrowFormat + " " + it.multilevel()).assertSuccess() }
    }

    @ParameterizedTest
    @MethodSource("delimiters")
    @DisplayName("Test suggest enum with different delimiters")
    fun testSuggestEnum(command: String, del: String) {
        platform.suggest("$command enum ")
            .assertSuggest("FIRST", "SECOND", "THIRD", "FOURTH")

        platform.suggest("$command enum FIRST${del}")
            .mapIf(del.contains(" ")) { "FIRST$del$it" }
            .assertAsSuggester(EnumArgumentResolver(), TEST_ENUM_ARGUMENT, { "FIRST$del$it" }, "")

        platform.suggest("$command enum FIRST")
            .assertSuggest("FIRST$del", "FIRST")
    }

}