package dev.rollczi.litecommands.complete

import dev.rollczi.litecommands.LiteCommandsSpec
import dev.rollczi.litecommands.component.LiteComponent
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Test

class SuggestionTest : LiteCommandsSpec() {

    private val section = factory.createSection(TestSuggestionCommand::class.java).get()

    private var invocationAc : LiteComponent.ContextOfResolving = contextCreator.apply("ac", arrayOf(""))

    @Test
    fun `without arg tabulation test`() {
        assertArrayEquals(arrayOf("help", "manage"), section.resolveCompletion(invocationAc).toTypedArray())
    }

    private var invocationAcHelp : LiteComponent.ContextOfResolving = contextCreator.apply("ac", arrayOf("help", ""))

    @Test
    fun `empty tabulation test`() {
        assertArrayEquals(emptyArray<String>(), section.resolveCompletion(invocationAcHelp).toTypedArray())
    }

    private var invocationAcManage : LiteComponent.ContextOfResolving = contextCreator.apply("ac", arrayOf("manage", ""))
    private var invocationAcMove : LiteComponent.ContextOfResolving = contextCreator.apply("ac", arrayOf("manage", "move", ""))

    @Test
    fun `inner section tabulation test` () {
        assertArrayEquals(arrayOf("move"), section.resolveCompletion(invocationAcManage).toTypedArray())
        assertArrayEquals(arrayOf("test1", "test2", "test3"), section.resolveCompletion(invocationAcMove).toTypedArray())
    }

    private var invocationAcMoveBack : LiteComponent.ContextOfResolving = contextCreator.apply("ac", arrayOf("manage", "mo"))
    private var invocationAcMoveIn : LiteComponent.ContextOfResolving = contextCreator.apply("ac", arrayOf("manage", "move"))

    @Test
    fun `back tabulation test` () {
        assertArrayEquals(arrayOf("move"), section.resolveCompletion(invocationAcMoveBack).toTypedArray())
        assertArrayEquals(arrayOf("move"), section.resolveCompletion(invocationAcMoveIn).toTypedArray())
    }

}