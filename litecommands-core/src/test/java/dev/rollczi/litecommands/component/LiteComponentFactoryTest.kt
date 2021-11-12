package dev.rollczi.litecommands.component

import dev.rollczi.litecommands.annotations.parser.LiteAnnotationParser
import dev.rollczi.litecommands.LiteCommandsSpec
import groovy.transform.CompileStatic
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory

@CompileStatic
class LiteComponentFactoryTest : LiteCommandsSpec() {

    @Test
    fun `component factory test`() {
        val parser = LiteAnnotationParser()
        val factory = LiteComponentFactory(
            LoggerFactory.getLogger("test"),
            injector,
            parser
        )

        val sectionOption = factory.createSection(TestCommand::class.java)

        assertTrue(sectionOption.isPresent)

        val section = sectionOption.get()
        val scope = section.scope

        assertEquals("ac", scope.name)
        assertEquals(3, section.resolvers.size)

        for (component: LiteComponent in section.resolvers) {
            if (component !is LiteSection) {
                continue
            }

            assertEquals("manage", component.scope.name)
        }

    }

}