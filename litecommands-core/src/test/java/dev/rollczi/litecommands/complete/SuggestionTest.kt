package dev.rollczi.litecommands.complete

import dev.rollczi.litecommands.EmptyTestSender
import dev.rollczi.litecommands.LiteCommandsSpec
import dev.rollczi.litecommands.LiteInvocation
import dev.rollczi.litecommands.component.LiteComponent
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Test
import java.util.function.BiFunction

class SuggestionTest : LiteCommandsSpec() {

    private var metaDataHelper : BiFunction<String, Array<String>, LiteComponent.MetaData> = BiFunction {
            command, args -> LiteComponent.MetaData.create(LiteInvocation(command, EmptyTestSender(), args))
    }

    private var invocationAc : LiteComponent.MetaData = metaDataHelper.apply("ac", arrayOf(""))
    private var invocationAcHelp : LiteComponent.MetaData = metaDataHelper.apply("ac", arrayOf("help", ""))
    private var invocationAcManage : LiteComponent.MetaData = metaDataHelper.apply("ac", arrayOf("manage", ""))
    private var invocationAcMove : LiteComponent.MetaData = metaDataHelper.apply("ac", arrayOf("manage", "move", ""))
    private var invocationAcMoveStart : LiteComponent.MetaData = metaDataHelper.apply("ac", arrayOf("manage", "mo"))
    private var invocationAcMoveIn : LiteComponent.MetaData = metaDataHelper.apply("ac", arrayOf("manage", "move"))

    @Test
    fun `test correctness of component executing`() {
        val section = factory.createSection(TestSuggestionCommand::class.java).get()

        assertArrayEquals(arrayOf("help", "manage"), section.resolveCompletion(invocationAc).toTypedArray())
        assertArrayEquals(emptyArray<String>(), section.resolveCompletion(invocationAcHelp).toTypedArray())
        assertArrayEquals(arrayOf("move"), section.resolveCompletion(invocationAcManage).toTypedArray())
        assertArrayEquals(arrayOf("test1", "test2", "test3"), section.resolveCompletion(invocationAcMove).toTypedArray())
        assertArrayEquals(arrayOf("move"), section.resolveCompletion(invocationAcMoveStart).toTypedArray())
        assertArrayEquals(arrayOf("move"), section.resolveCompletion(invocationAcMoveIn).toTypedArray())
    }

}