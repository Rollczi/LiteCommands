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

    public Result<LiteSection, Throwable> createSection(Object sectionInstance) {
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
                    .map(innerClass -> createSection(innerClass)
                            .orElseThrow(error -> new RuntimeException("Can't create inner class " + innerClass, error)))
                    .toSet();

            return LiteSection.builder()
                    .scopeInformation(scope)
                    .resolvers(innerSections)
                    .resolvers(executions)
                    .build();
        }).toResult(new RuntimeException(sectionInstance.getClass() + " class isn't a section"));
    }

    public Result<LiteSection, Throwable> createSection(Class<?> sectionClass) {
        return Result.attempt(Throwable.class, () -> injector.newInstance(sectionClass))
                .flatMap(this::createSection);
    }

    public Option<LiteExecution> createExecution(Object instance, Method executionMethod) {
        MethodExecutor methodExecutor = new MethodExecutor(executionMethod, instance, injector, parser);

        return parser.parse(executionMethod)
                .map((scope) -> new LiteExecution(logger, parser, scope, methodExecutor));
    }

}
