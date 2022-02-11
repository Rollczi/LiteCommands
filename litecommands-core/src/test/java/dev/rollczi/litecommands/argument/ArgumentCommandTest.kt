package dev.rollczi.litecommands.argument

import dev.rollczi.litecommands.LiteCommandsSpec
import dev.rollczi.litecommands.LiteTestFactory
import dev.rollczi.litecommands.LiteTestPlatform
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import panda.std.Option

class ArgumentCommandTest : LiteCommandsSpec() {

    @Test
    fun `test correctness of component executing`() {
        val testCommand = TestCommand()

        val liteCommands = LiteTestFactory.builder()
            .argument(String::class.java, StringArg())
            .argument(String::class.java, CustomStringArg())
            .argument(Option::class.java, StringArg().toOptionHandler())
            .argument(Option::class.java, DoubleArg().toOptionHandler())
            .commandInstance(testCommand)
            .register()

        val platform: LiteTestPlatform = liteCommands.getPlatformManager()

        platform.invocation("test", arrayOf("option", "test"))
        platform.invocation("test", arrayOf("value_option", "test"))
        platform.invocation("test", arrayOf("option_option", "0.0", "test"))
        platform.invocation("test", arrayOf("customstring_string", "test", "test"))
        platform.invocation("test", arrayOf("string_customstring", "test", "test"))

        assertEquals(1, testCommand.resultOption.executions)
        assertEquals(1, testCommand.resultValueOption.executions)
        assertEquals(1, testCommand.resultOptionOption.executions)
        assertEquals("test", testCommand.resultCustom1.string)
        assertEquals("<test>", testCommand.resultCustom1.customString)
        assertEquals("test", testCommand.resultCustom2.string)
        assertEquals("<test>", testCommand.resultCustom2.customString)
    }

}