package dev.rollczi.litecommands.annotations.argument;

import dev.rollczi.litecommands.annotations.AnnotationInvoker;
import dev.rollczi.litecommands.annotations.AnnotationProcessor;
import dev.rollczi.litecommands.argument.resolver.standard.EnumArgumentResolver;
import dev.rollczi.litecommands.requirement.Requirement;

public class CaseInsensitiveAnnotationResolver<SENDER> implements AnnotationProcessor<SENDER> {

    @Override
    public AnnotationInvoker<SENDER> process(AnnotationInvoker<SENDER> invoker) {
        return invoker.onParameterRequirement(CaseInsensitive.class, this::resolve);
    }

    private void resolve(Object parameter, CaseInsensitive annotation, Object builder, Requirement<?> requirement) {
        requirement.meta().put(EnumArgumentResolver.CASE_INSENSITIVE, true);
    }

}
