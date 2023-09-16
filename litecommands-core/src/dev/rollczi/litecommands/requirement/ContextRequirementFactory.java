package dev.rollczi.litecommands.requirement;

import dev.rollczi.litecommands.context.ContextRegistry;
import dev.rollczi.litecommands.annotation.AnnotationHolder;
import dev.rollczi.litecommands.wrapper.Wrapper;
import dev.rollczi.litecommands.wrapper.WrapperRegistry;
import dev.rollczi.litecommands.wrapper.WrapFormat;

import java.lang.annotation.Annotation;

class ContextRequirementFactory<SENDER, A extends Annotation> implements RequirementFactory<SENDER, A> {

    private final ContextRegistry<SENDER> contextRegistry;
    private final WrapperRegistry wrapperRegistry;

    public ContextRequirementFactory(ContextRegistry<SENDER> contextRegistry, WrapperRegistry wrapperRegistry) {
        this.contextRegistry = contextRegistry;
        this.wrapperRegistry = wrapperRegistry;
    }

    @Override
    public <PARSED> Requirement<SENDER, PARSED> create(AnnotationHolder<A, PARSED, ?> holder) {
        WrapFormat<PARSED, ?> wrapFormat = holder.getFormat();
        Wrapper wrapper = wrapperRegistry.getWrappedExpectedFactory(wrapFormat);
        return new ContextRequirementImpl<>(this.contextRegistry, holder, wrapper);
    }

}
