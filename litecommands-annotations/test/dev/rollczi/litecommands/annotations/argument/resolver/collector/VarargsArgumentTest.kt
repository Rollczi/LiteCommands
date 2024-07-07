package dev.rollczi.litecommands.annotations.argument.resolver.collector

import dev.rollczi.litecommands.annotations.LiteTestSpec
import dev.rollczi.litecommands.annotations.argument.Arg
import dev.rollczi.litecommands.annotations.command.Command
import dev.rollczi.litecommands.annotations.execute.Execute
import dev.rollczi.litecommands.annotations.varargs.Varargs
import dev.rollczi.litecommands.argument.resolver.standard.NumberArgumentResolver
import dev.rollczi.litecommands.invalidusage.InvalidUsage.Cause
import dev.rollczi.litecommands.suggestion.Suggestion
import dev.rollczi.litecommands.unit.AssertExecute
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import panda.std.Pair
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Optional
import java.util.stream.Stream

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class VarargsArgumentTest : LiteTestSpec() {

    private val dateFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

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
            .assertAsSuggester(NumberArgumentResolver.ofInteger(), { suggest: String ->
                var suggestion = "1$delimiter$suggest"

                if (delimiter.contains(" ")) { // TODO Currently, LiteCommands filters all completions on the left side of the space character. Add option to disable this behavior.
                    suggestion = suggestion.substring(suggestion.lastIndexOf(" ") + 1)
                }

                suggestion
            }, "")

        platform.suggest("$command Integer 5")
            .assertSuggestAndFlush("5$delimiter", "5")
            .assertAsSuggester(NumberArgumentResolver.ofInteger(), "5")
    }

    @ParameterizedTest
    @MethodSource("delimiters")
    @Disabled
    @DisplayName("Test suggest string with different delimiters")
    fun testSuggestString(command: String, delimiter: String) {
        platform.suggest("$command String ")
            .assertSuggest("<argument>")
        platform.suggest("$command String <argument>")
            .assertSuggest("<argument>", "<argument>$delimiter")
        platform.suggest("$command String text$delimiter")
            .assertSuggest("text$delimiter", "text$delimiter<argument>")
        platform.suggest("$command String text")
            .assertSuggest("text", "text$delimiter")
    }

    @ParameterizedTest
    @MethodSource("provideDurationArgs")
    @Disabled
    fun testSuggestDuration(input: String, expectedSuggestions: List<String>) {
        platform.suggest(input).assertSuggest(expectedSuggestions)
    }

    private fun provideDurationArgs(): Stream<Arguments> {
        return Stream.of(
            Arguments.of("test Duration ", listOf("1s", "1d", "1h", "1m", "30d", "10h", "7d", "10m", "30m", "5h", "10s", "30s", "5m", "5s", "1m30s")),
            Arguments.of("test Duration 10", listOf("10s", "10m", "10h")),
            Arguments.of("test Duration 1m", listOf("1m", "1m,", "1m30s")),
            Arguments.of(
                "test Duration 10h,",
                listOf(
                    "10h,1s",
                    "10h,1m",
                    "10h,1d",
                    "10h,1h",
                    "10h,30d",
                    "10h,10h",
                    "10h,7d",
                    "10h,10m",
                    "10h,30m",
                    "10h,5h",
                    "10h,10s",
                    "10h,30s",
                    "10h,5m",
                    "10h,5s",
                    "10h,1m30s"
                )
            ),
            Arguments.of(
                "test Duration 10h,1",
                listOf(
                    "10h,1s",
                    "10h,1m",
                    "10h,1d",
                    "10h,1h",
                    "10h,10h",
                    "10h,10m",
                    "10h,10s",
                    "10h,1m30s"
                )
            ),
            Arguments.of("test Duration 10h,1m", listOf("10h,1m", "10h,1m,", "10h,1m30s")),
            Arguments.of(
                "test Duration 10h,1m,",
                listOf(
                    "10h,1m,1s",
                    "10h,1m,1m",
                    "10h,1m,1d",
                    "10h,1m,1h",
                    "10h,1m,30d",
                    "10h,1m,10h",
                    "10h,1m,7d",
                    "10h,1m,10m",
                    "10h,1m,30m",
                    "10h,1m,5h",
                    "10h,1m,10s",
                    "10h,1m,30s",
                    "10h,1m,5m",
                    "10h,1m,5s",
                    "10h,1m,1m30s"
                )
            ),
            Arguments.of(
                "test Duration 10h,1m,1",
                listOf(
                    "10h,1m,1s",
                    "10h,1m,10s",
                    "10h,1m,1m",
                    "10h,1m,1d",
                    "10h,1m,1h",
                    "10h,1m,10h",
                    "10h,1m,10m",
                    "10h,1m,1m30s"
                )
            ),
            Arguments.of("test Duration 10h,1m,2m", listOf("10h,1m,2m", "10h,1m,2m,"))
        )
    }

    @Test
    @Disabled
    fun testSuggestInstantSpecial() {
        platform.suggest("test Instant-special ")
            .assertNotEmpty()
            .assertCorrect { suggestion: Suggestion ->
                val multilevel = suggestion.multilevel()
                if (multilevel == "-") {
                    return@assertCorrect
                }

                if (multilevel.chars().allMatch { codePoint: Int -> Character.isDigit(codePoint) }) {
                    platform.execute("test Instant-special $multilevel").assertSuccess()
                    return@assertCorrect
                }
                platform.execute("test Instant-special $multilevel 5").assertSuccess()
            }
    }


    @Test
    @Disabled
    fun testSuggestInstant() {
        platform.suggest("test Instant ")
            .assertNotEmpty().assertCorrect { suggestion: Suggestion ->
                platform.execute(
                    "test Instant " + suggestion.multilevel()
                ).assertSuccess()
            }

        val tomorrow = LocalDate.now().plusDays(1)
        val tomorrowFormat = dateFormat.format(tomorrow)

        platform.suggest("test Instant " + tomorrow.year)
            .assertNotEmpty().assertCorrect { suggestion: Suggestion ->
                platform.execute(
                    "test Instant " + suggestion.multilevel()
                ).assertSuccess()
            }
        platform.suggest("test Instant $tomorrowFormat ")
            .assertNotEmpty().assertCorrect { suggestion: Suggestion ->
                platform.execute(
                    "test Instant " + tomorrowFormat + " " + suggestion.multilevel()
                ).assertSuccess()
            }
        platform.suggest("test Instant 2021-01-01 00:05:50,2023-04-17 11:03:00,$tomorrowFormat ")
            .assertNotEmpty().assertCorrect { suggestion: Suggestion ->
                platform.execute(
                    "test Instant " + tomorrowFormat + " " + suggestion.multilevel()
                ).assertSuccess()
            }
    }

    @Test
    @Disabled
    fun testSuggestEnum() {
        platform.suggest("test enum ")
            .assertSuggest("FIRST", "SECOND", "THIRD", "FOURTH")
        platform.suggest("test enum FIRST,")
            .assertSuggest("FIRST,FIRST", "FIRST,SECOND", "FIRST,THIRD", "FIRST,FOURTH")
        platform.suggest("test enum FIRST")
            .assertSuggest("FIRST,", "FIRST")
    }

}