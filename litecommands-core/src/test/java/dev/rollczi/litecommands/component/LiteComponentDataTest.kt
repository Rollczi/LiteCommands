package dev.rollczi.litecommands.component

import dev.rollczi.litecommands.LiteCommandsSpec
import dev.rollczi.litecommands.LiteInvocation
import groovy.transform.CompileStatic
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import panda.utilities.StringUtils

@CompileStatic
class LiteComponentDataTest : LiteCommandsSpec() {

    private var dataAc : LiteComponent.Data = LiteComponent.Data.create(
        LiteInvocation(
            "ac",
            EmptyTestSender(), arrayOf()
        )
    )
    private var dataAcWithArgs : LiteComponent.Data = LiteComponent.Data.create(
        LiteInvocation(
            "ac",
            EmptyTestSender(), arrayOf("test")
        )
    )
    private var dataAcHelp : LiteComponent.Data = LiteComponent.Data.create(
        LiteInvocation(
            "ac",
            EmptyTestSender(), arrayOf("help")
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

    class TestComponent(private val name: String) : LiteComponent {

        override fun resolve(data: LiteComponent.Data) {
            data.resolverNestingTracing(this)
        }

        override fun getScope(): ScopeMetaData {
            return ScopeMetaData.builder()
                .name(name)
                .build()
        }

    }

}