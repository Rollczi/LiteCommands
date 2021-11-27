package dev.rollczi.litecommands.multisections

import dev.rollczi.litecommands.LiteCommandsSpec
import dev.rollczi.litecommands.LiteRegisterResolvers
import dev.rollczi.litecommands.component.LiteMultiSection
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Test
import java.lang.RuntimeException

class MultiSectionTest : LiteCommandsSpec() {

    @Test
    fun `test correctness of component executing`() {
        val registerResolvers = LiteRegisterResolvers()

        registerResolvers.register(factory.createSection(MultiSectionTestCommand::class.java).get())
        registerResolvers.register(factory.createSection(AppendMultiSectionTestCommand::class.java).get())

        assertEquals(1, registerResolvers.resolvers.size)

        val liteComponent = registerResolvers.resolvers["ac"]

        assertInstanceOf(LiteMultiSection::class.java, liteComponent)

        if (liteComponent is LiteMultiSection) {
            assertEquals(2, liteComponent.sections.size)

            assertEquals("ac", liteComponent.resolveExecution(contextCreator.apply("ac", arrayOf())).validMessage)
            assertEquals("tp", liteComponent.resolveExecution(contextCreator.apply("ac", arrayOf("tp"))).validMessage)
            assertEquals("help", liteComponent.resolveExecution(contextCreator.apply("ac", arrayOf("help"))).validMessage)

        } else throw RuntimeException()
    }

}