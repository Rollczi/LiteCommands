package dev.rollczi.litecommands.multisections

import dev.rollczi.litecommands.LiteCommandsSpec
import dev.rollczi.litecommands.LiteRegisterResolvers
import dev.rollczi.litecommands.component.LiteSection
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.lang.RuntimeException

class MultiSectionTest : LiteCommandsSpec() {

    @Test
    fun `test correctness of component executing`() {
        val registerResolvers = LiteRegisterResolvers()

        registerResolvers.register(factory.createSection(AppendMultiSectionTestCommand::class.java).get())
        registerResolvers.register(factory.createSection(MultiSectionTestCommand::class.java).get())

        assertEquals(1, registerResolvers.resolvers.size)

        val liteComponent = registerResolvers.resolvers["ac"]

        if (liteComponent is LiteSection) {
            assertEquals(3, liteComponent.resolvers.size)

            assertEquals("ac", liteComponent.resolveExecution(contextCreator.apply("ac", arrayOf())).validMessage)
            assertEquals("tp", liteComponent.resolveExecution(contextCreator.apply("ac", arrayOf("tp"))).validMessage)
            assertEquals("help", liteComponent.resolveExecution(contextCreator.apply("ac", arrayOf("help"))).validMessage)

        } else throw RuntimeException()
    }

}