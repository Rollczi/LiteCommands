package dev.rollczi.litecommands

import dev.rollczi.litecommands.annotations.parser.LiteAnnotationParser
import dev.rollczi.litecommands.component.LiteComponentFactory
import groovy.transform.CompileStatic
import org.panda_lang.utilities.inject.DependencyInjection
import org.panda_lang.utilities.inject.Injector
import java.util.logging.Logger

@CompileStatic
open class LiteCommandsSpec {

    protected var injector: Injector = DependencyInjection.createInjector()
    protected var factory = LiteComponentFactory(
        Logger.getLogger("LiteCommandsSpec"),
        injector,
        LiteAnnotationParser(mapOf(Pair(EmptyTestSender::class.java, EmptyTestSenderArgument())))
    )

}
