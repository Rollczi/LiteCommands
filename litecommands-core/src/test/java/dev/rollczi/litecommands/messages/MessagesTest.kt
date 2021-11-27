package dev.rollczi.litecommands.messages

import dev.rollczi.litecommands.LiteCommandsSpec
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MessagesTest : LiteCommandsSpec() {

    @Test
    fun `test correctness of component executing`() {
        val section = factory.createSection(MessagesTestCommand::class.java).get()

        assertEquals("out", section.resolveExecution(contextCreator.apply("out", arrayOf("empty"))).validMessage)
        assertEquals("in", section.resolveExecution(contextCreator.apply("out", arrayOf("in", "empty"))).validMessage)
        assertEquals("execute", section.resolveExecution(contextCreator.apply("out", arrayOf("in", "execute"))).validMessage)
    }

}