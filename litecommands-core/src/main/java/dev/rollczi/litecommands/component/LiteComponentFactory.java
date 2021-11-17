package dev.rollczi.litecommands.component;

import dev.rollczi.litecommands.annotations.parser.AnnotationParser;
import org.panda_lang.utilities.inject.Injector;
import panda.std.Option;
import panda.std.Result;
import panda.std.stream.PandaStream;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.logging.Logger;

public class LiteComponentFactory {

    private final Logger logger;
    private final Injector injector;
    private final AnnotationParser parser;

    public LiteComponentFactory(Logger logger, Injector injector, AnnotationParser parser) {
        this.logger = logger;
        this.injector = injector;
        this.parser = parser;
    }

    public Option<LiteSection> createSection(Object sectionInstance) {
        Class<?> sectionClass = sectionInstance.getClass();

        return parser.parse(sectionClass).map(scope -> {
            Set<LiteExecution> executions = PandaStream.of(sectionClass.getDeclaredMethods())
                    .concat(sectionClass.getDeclaredMethods())
                    .distinct()
                    .mapOpt(method -> createExecution(sectionInstance, method))
                    .toSet();

            Set<LiteSection> innerSections = PandaStream.of(sectionClass.getClasses())
                    .concat(sectionClass.getDeclaredClasses())
                    .distinct()
                    .mapOpt(innerClass -> Result.attempt(Throwable.class, () -> createSection(innerClass))
                            .orElseThrow(error -> new RuntimeException("Can't create inner class " + innerClass, error)))
                    .toSet();

            return LiteSection.builder()
                    .scopeInformation(scope)
                    .resolvers(innerSections)
                    .resolvers(executions)
                    .build();
        });
    }

    public Option<LiteSection> createSection(Class<?> sectionClass) throws Throwable {
        return createSection(injector.newInstance(sectionClass));
    }

    public Option<LiteExecution> createExecution(Object instance, Method executionMethod) {
        return parser.parse(executionMethod)
                .map((scope) -> new LiteExecution(logger, parser, scope, new MethodExecutor(executionMethod, instance, injector)));
    }

}
