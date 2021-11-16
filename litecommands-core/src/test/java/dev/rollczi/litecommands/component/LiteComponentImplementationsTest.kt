package dev.rollczi.litecommands.component

import dev.rollczi.litecommands.EmptyTestSender
import dev.rollczi.litecommands.LiteCommandsSpec
import dev.rollczi.litecommands.LiteInvocation
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class LiteComponentImplementationsTest : LiteCommandsSpec() {

    // on command invocation /ac
    private var invocationAc : LiteComponent.MetaData = LiteComponent.MetaData.create(
        LiteInvocation(
            "ac",
            EmptyTestSender(), arrayOf()
        )
    )
    // on command invocation /ac test
    private var invocationAcWithArgs : LiteComponent.MetaData = LiteComponent.MetaData.create(
        LiteInvocation(
            "ac",
            EmptyTestSender(), arrayOf("test")
        )
    )
    // on command invocation /ac help
    private var invocationAcHelp : LiteComponent.MetaData = LiteComponent.MetaData.create(
        LiteInvocation(
            "ac",
            EmptyTestSender(), arrayOf("help")
        )
    )

    @Test
    fun `test correctness of component executing`() {
        val command = TestCommand()
        val section = factory.createSection(command).get()

        section.resolveExecution(invocationAc)

        assertEquals(1, command.executeAc)
        assertEquals(0, command.executeHelp)

        section.resolveExecution(invocationAcWithArgs)
        section.resolveExecution(invocationAcHelp)

        assertEquals(2, command.executeAc)
        assertEquals(1, command.executeHelp)
    }

}