package dev.rollczi.litecommands.scheme

import dev.rollczi.litecommands.*
import dev.rollczi.litecommands.argument.StringArg
import dev.rollczi.litecommands.valid.messages.UseSchemeFormatting
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import panda.std.Option

class SchemeUseCommandTest : LiteCommandsSpec() {

    private val testCommand = TestSchemeUseCommand()
    private val liteCommands: LiteCommands = LiteTestFactory.builder()
        .argument(String::class.java, StringArg())
        .argument(Option::class.java, StringArg().toOptionHandler())
        .commandInstance(testCommand)
        .executionResultHandler(LiteTestExecutionResultHandler())
        .register()

    private val platform: LiteTestPlatform = liteCommands.getPlatformManager()

    @Test
    fun `test normal generating command use scheme`() {
        val tester = TestSchemeMessage(UseSchemeFormatting.NORMAL)

        assertEquals("/command hello/hey/siema", tester.message(platform.invocation("command")))
        assertEquals("/command hello", tester.message(platform.invocation("command", "hello")))
        assertEquals("/command hey <text> <text>", tester.message(platform.invocation("command", "hey")))
        assertEquals("/command siema <text> [text]", tester.message(platform.invocation("command", "siema")))
    }

    @Test
    fun `test revert optional generating command use scheme`() {
        val tester = TestSchemeMessage(UseSchemeFormatting.REVERT_OPTIONAL)

        assertEquals("/command hello/hey/siema", tester.message(platform.invocation("command")))
        assertEquals("/command hello", tester.message(platform.invocation("command", "hello")))
        assertEquals("/command hey [text] [text]", tester.message(platform.invocation("command", "hey")))
        assertEquals("/command siema [text] <text>", tester.message(platform.invocation("command", "siema")))
    }

    @Test
    fun `test pipe generating command use scheme`() {
        val tester = TestSchemeMessage(UseSchemeFormatting.PIPE)

        assertEquals("/command hello|hey|siema", tester.message(platform.invocation("command")))
        assertEquals("/command hello", tester.message(platform.invocation("command", "hello")))
        assertEquals("/command hey <text> <text>", tester.message(platform.invocation("command", "hey")))
        assertEquals("/command siema <text> [text]", tester.message(platform.invocation("command", "siema")))
    }

    @Test
    fun `test pipe and revert optional generating command use scheme`() {
        val tester = TestSchemeMessage(UseSchemeFormatting.PIPE_AND_REVERT_OPTIONAL)

        assertEquals("/command hello|hey|siema", tester.message(platform.invocation("command")))
        assertEquals("/command hello", tester.message(platform.invocation("command", "hello")))
        assertEquals("/command hey [text] [text]",  tester.message(platform.invocation("command", "hey")))
        assertEquals("/command siema [text] <text>", tester.message(platform.invocation("command", "siema")))
    }

}