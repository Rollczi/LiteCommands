package dev.rollczi.litecommands.joiner

import dev.rollczi.litecommands.LiteCommandsSpec
import dev.rollczi.litecommands.LiteTestFactory
import dev.rollczi.litecommands.LiteTestPlatform
import dev.rollczi.litecommands.annotations.Execute
import dev.rollczi.litecommands.annotations.Joiner
import dev.rollczi.litecommands.annotations.Section
import dev.rollczi.litecommands.argument.StringArg
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import panda.std.Option

class JoinerCommandTest : LiteCommandsSpec() {

    private val testCommand = JoinerTestCommand()
    private val liteCommands = LiteTestFactory.builder()
        .argument(String::class.java, StringArg())
        .commandInstance(testCommand)
        .register()

    private val platform: LiteTestPlatform = liteCommands.getPlatformManager()

    @Test
    fun `joiner test`() {
        platform.invocation("test", arrayOf())
        assertEquals("", JoinerTestCommand.result)

        platform.invocation("test", arrayOf("value1"))
        assertEquals("value1", JoinerTestCommand.result)

        platform.invocation("test", arrayOf("value1", "value2"))
        assertEquals("value1 value2", JoinerTestCommand.result)
    }

    @Test
    fun `joiner test with subcommand and argument`() {

        platform.invocation("test", arrayOf("single", "X1"))
        assertEquals("", JoinerTestCommand.result)
        assertEquals("X1", JoinerTestCommand.arg1)

        platform.invocation("test", arrayOf("single", "Y1", "value1"))
        assertEquals("value1", JoinerTestCommand.result)
        assertEquals("Y1", JoinerTestCommand.arg1)

        platform.invocation("test", arrayOf("single", "Z1", "value1", "value2"))
        assertEquals("value1 value2", JoinerTestCommand.result)
        assertEquals("Z1", JoinerTestCommand.arg1)
    }

    @Test
    fun `joiner test with subcommand and two arguments`() {
        platform.invocation("test", arrayOf("double", "X1", "X2"))
        assertEquals("", JoinerTestCommand.result)
        assertEquals("X1", JoinerTestCommand.arg1)
        assertEquals("X2", JoinerTestCommand.arg2)

        platform.invocation("test", arrayOf("double", "Y1", "Y2", "value1"))
        assertEquals("value1", JoinerTestCommand.result)
        assertEquals("Y1", JoinerTestCommand.arg1)
        assertEquals("Y2", JoinerTestCommand.arg2)

        platform.invocation("test", arrayOf("double", "Z1", "Z2", "value1", "value2"))
        assertEquals("value1 value2", JoinerTestCommand.result)
        assertEquals("Z1", JoinerTestCommand.arg1)
        assertEquals("Z2", JoinerTestCommand.arg2)
    }

    @Test
    fun `joiner test with section`() {
        platform.invocation("test", arrayOf("section"))
        assertEquals("", JoinerTestCommand.result)

        platform.invocation("test", arrayOf("section", "A1"))
        assertEquals("A1", JoinerTestCommand.result)

        platform.invocation("test", arrayOf("section", "A1", "B2"))
        assertEquals("A1 B2", JoinerTestCommand.result)
    }

    @Test
    fun `joiner test with section and subcommand and argument`() {
        platform.invocation("test", arrayOf("section", "single", "X1"))
        assertEquals("", JoinerTestCommand.result)
        assertEquals("X1", JoinerTestCommand.arg1)

        platform.invocation("test", arrayOf("section", "single", "X2", "test"))
        assertEquals("test", JoinerTestCommand.result)
        assertEquals("X2", JoinerTestCommand.arg1)

        platform.invocation("test", arrayOf("section", "single", "X3", "test", "test"))
        assertEquals("test test", JoinerTestCommand.result)
        assertEquals("X3", JoinerTestCommand.arg1)
    }

}