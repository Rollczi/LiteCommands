package dev.rollczi.litecommands.component

import dev.rollczi.litecommands.LiteTestSender
import dev.rollczi.litecommands.LiteCommandsSpec
import dev.rollczi.litecommands.LiteInvocation
import dev.rollczi.litecommands.scope.ScopeMetaData
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import panda.utilities.StringUtils
import java.util.*

class LiteComponentContextOfResolvingTest : LiteCommandsSpec() {

    private var dataAc : LiteComponent.ContextOfResolving = LiteComponent.ContextOfResolving.create(
        LiteInvocation(
            "ac",
            LiteTestSender(), arrayOf()
        )
    )
    private var dataAcWithArgs : LiteComponent.ContextOfResolving = LiteComponent.ContextOfResolving.create(
        LiteInvocation(
            "ac",
            LiteTestSender(), arrayOf("test")
        )
    )
    private var dataAcHelp : LiteComponent.ContextOfResolving = LiteComponent.ContextOfResolving.create(
        LiteInvocation(
            "ac",
            LiteTestSender(), arrayOf("help")
        )
    )

    @Test
    fun `component data test`() {
        val componentAc = TestComponent("ac")

        dataAc = dataAc.resolverNestingTracing(componentAc)
        assertEquals(0, dataAc.getCurrentArgsCount(TestComponent(StringUtils.EMPTY)))


        dataAcWithArgs = dataAcWithArgs.resolverNestingTracing(componentAc)
        assertEquals(1, dataAcWithArgs.getCurrentArgsCount(TestComponent(StringUtils.EMPTY)))

        dataAcHelp = dataAcHelp.resolverNestingTracing(componentAc)
        assertEquals(0, dataAcHelp.getCurrentArgsCount(TestComponent("help")))
    }

    class TestComponent(name: String)
        : AbstractComponent(ScopeMetaData.builder().name(name).build()) {

        override fun resolveExecution(data: LiteComponent.ContextOfResolving) : ExecutionResult {
            return ExecutionResult.valid(data.resolverNestingTracing(this))
        }

        override fun resolveCompletion(data: LiteComponent.ContextOfResolving): MutableList<String> = Collections.emptyList()

    }

}