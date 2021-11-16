package dev.rollczi.litecommands

import dev.rollczi.litecommands.annotations.parser.LiteAnnotationParser
import dev.rollczi.litecommands.component.LiteComponentFactory
import groovy.transform.CompileStatic
import org.panda_lang.utilities.inject.DependencyInjection
import org.panda_lang.utilities.inject.Injector
import org.slf4j.LoggerFactory

@CompileStatic
open class LiteCommandsSpec {

    protected var injector: Injector = DependencyInjection.createInjector()
    protected var factory = LiteComponentFactory(
        LoggerFactory.getLogger(LiteCommandsSpec::class.java),
        injector,
        LiteAnnotationParser(mapOf(Pair(EmptyTestSender::class.java, EmptyTestSenderArgument())))
    )

}
