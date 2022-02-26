package dev.rollczi.litecommands.complete

import dev.rollczi.litecommands.LiteCommandsSpec
import dev.rollczi.litecommands.LiteTestFactory
import dev.rollczi.litecommands.LiteTestPlatform
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class SuggestionTest : LiteCommandsSpec() {

    private val testSuggestionCommand = TestSuggestionCommand()
    private val testSuggestionCommandWithJoiner = TestSuggestionCommandWithJoiner()
    private val liteCommands = LiteTestFactory.builder()
        .commandInstance(testSuggestionCommand)
        .commandInstance(testSuggestionCommandWithJoiner)
        .register()

    private val platform: LiteTestPlatform = liteCommands.getPlatformManager()

    @Test
    fun `without arg tabulation test`() {
        assertArrayEquals(arrayOf("help", "manage"), platform.suggestion("ac", arrayOf("")).toTypedArray())
    }

    @Test
    fun `empty tabulation test`() {
        assertArrayEquals(emptyArray<String>(), platform.suggestion("ac", arrayOf("help", "")).toTypedArray())
    }

    @Test
    fun `inner section tabulation test` () {
        assertArrayEquals(arrayOf("move"), platform.suggestion("ac", arrayOf("manage", "")).toTypedArray())
        assertArrayEquals(arrayOf("test1", "test2", "test3"), platform.suggestion("ac", arrayOf("manage", "move", "")).toTypedArray())
    }

    @Test
    fun `back tabulation test` () {
        assertArrayEquals(arrayOf("move"), platform.suggestion("ac", arrayOf("manage", "mo")).toTypedArray())
        assertArrayEquals(arrayOf("move"), platform.suggestion("ac", arrayOf("manage", "move")).toTypedArray())
    }

    @Test
    fun `completion with joiner` () {
        assertEquals(0, platform.suggestion("joiner", arrayOf("test")).size)
        assertEquals(0, platform.suggestion("joiner", arrayOf("test", "test")).size)
        assertEquals(0, platform.suggestion("joiner", arrayOf("test", "test", "test")).size)
    }

}