package dev.rollczi.litecommands.scheme

import dev.rollczi.litecommands.*
import dev.rollczi.litecommands.argument.StringArg
import dev.rollczi.litecommands.component.LiteSection
import dev.rollczi.litecommands.valid.messages.UseSchemeFormatting
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import panda.std.Option
import panda.utilities.text.Joiner
import java.util.function.Function

class SchemeUseCommandTest : LiteCommandsSpec() {

    private val testCommand = TestSchemeUseCommand()
    private val liteCommands: LiteCommands = LiteTestFactory.builder()
        .argument(String::class.java, StringArg())
        .argument(Option::class.java, StringArg().toOptionHandler())
        .commandInstance(testCommand)
        .executionResultHandler(LiteTestExecutionResultHandler())
        .register()

    private val platform: LiteTestPlatform = liteCommands.getPlatformManager()
    private val resolvers = (liteCommands.registerResolvers.resolvers["command"] as LiteSection).resolvers

    @Test
    fun `test normal generating command use scheme`() {
        val tester = TestSchemeMessage(UseSchemeFormatting.NORMAL)
        val main = Joiner.on("/").join(resolvers) { x -> x.scope.name }

        assertEquals("/command $main", tester.message(platform.invocation("command")))
        assertEquals("/command hello", tester.message(platform.invocation("command", "hello")))
        assertEquals("/command hey <text> <text>", tester.message(platform.invocation("command", "hey")))
        assertEquals("/command siema <text> [text]", tester.message(platform.invocation("command", "siema")))
        assertEquals("/command custom <custom>", tester.message(platform.invocation("custom", "test")))
        assertEquals("/command custominarg <siema>", tester.message(platform.invocation("custominarg", "test")))
    }

    @Test
    fun `test revert optional generating command use scheme`() {
        val tester = TestSchemeMessage(UseSchemeFormatting.REVERT_OPTIONAL)
        val main = Joiner.on("/").join(resolvers) { x -> x.scope.name }

        assertEquals("/command $main", tester.message(platform.invocation("command")))
        assertEquals("/command hello", tester.message(platform.invocation("command", "hello")))
        assertEquals("/command hey [text] [text]", tester.message(platform.invocation("command", "hey")))
        assertEquals("/command siema [text] <text>", tester.message(platform.invocation("command", "siema")))
    }

    @Test
    fun `test pipe generating command use scheme`() {
        val tester = TestSchemeMessage(UseSchemeFormatting.PIPE)
        val main = Joiner.on("|").join(resolvers) { x -> x.scope.name }

        assertEquals("/command $main", tester.message(platform.invocation("command")))
        assertEquals("/command hello", tester.message(platform.invocation("command", "hello")))
        assertEquals("/command hey <text> <text>", tester.message(platform.invocation("command", "hey")))
        assertEquals("/command siema <text> [text]", tester.message(platform.invocation("command", "siema")))
    }

    @Test
    fun `test pipe and revert optional generating command use scheme`() {
        val tester = TestSchemeMessage(UseSchemeFormatting.PIPE_AND_REVERT_OPTIONAL)
        val main = Joiner.on("|").join(resolvers) { x -> x.scope.name }

        assertEquals("/command $main", tester.message(platform.invocation("command")))
        assertEquals("/command hello", tester.message(platform.invocation("command", "hello")))
        assertEquals("/command hey [text] [text]",  tester.message(platform.invocation("command", "hey")))
        assertEquals("/command siema [text] <text>", tester.message(platform.invocation("command", "siema")))
    }

}