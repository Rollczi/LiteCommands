package dev.rollczi.litecommands.requirement;

import dev.rollczi.litecommands.annotation.processor.AnnotationInvoker;
import dev.rollczi.litecommands.annotation.processor.AnnotationProcessor;
import dev.rollczi.litecommands.argument.parser.ParserRegistry;
import dev.rollczi.litecommands.context.ContextRegistry;
import dev.rollczi.litecommands.argument.ArgumentFactory;
import dev.rollczi.litecommands.wrapper.WrapperRegistry;

import java.lang.annotation.Annotation;

public class RequirementProcessor<SENDER, A extends Annotation> implements AnnotationProcessor<SENDER> {

    private final RequirementFactory<SENDER, A> requirementFactory;
    private final Class<A> annotationClass;

    public RequirementProcessor(WrapperRegistry wrapperRegistry, ParserRegistry<SENDER> parserRegistry, Class<A> annotationClass, ArgumentFactory<A> argumentFactory) {
        this.requirementFactory =  new ArgumentRequirementFactory<>(wrapperRegistry, parserRegistry, argumentFactory);
        this.annotationClass = annotationClass;
    }

    public RequirementProcessor(ContextRegistry<SENDER> contextRegistry, WrapperRegistry wrapperRegistry, Class<A> annotationClass) {
        this.requirementFactory = new ContextRequirementFactory<>(contextRegistry, wrapperRegistry);
        this.annotationClass = annotationClass;
    }

    @Override
    public AnnotationInvoker<SENDER> process(AnnotationInvoker<SENDER> invoker) {
        return invoker.onRequirement(annotationClass, (holder, builder, executorBuilder) -> {
            Requirement<SENDER, ?> requirement = requirementFactory.create(holder);
            executorBuilder.addRequirement(requirement);

            return builder;
        });
    }

}
