package dev.rollczi.litecommands.requirement;

import dev.rollczi.litecommands.argument.parser.input.ParseableInputMatcher;
import dev.rollczi.litecommands.context.ContextRegistry;
import dev.rollczi.litecommands.context.ContextResult;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.meta.MetaHolder;
import dev.rollczi.litecommands.annotation.AnnotationHolder;
import dev.rollczi.litecommands.wrapper.WrapFormat;
import dev.rollczi.litecommands.wrapper.Wrapper;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;

class ContextRequirementImpl<SENDER, PARSED, A extends Annotation> implements ContextRequirement<SENDER, PARSED> {

    private final ContextRegistry<SENDER> contextRegistry;
    private final AnnotationHolder<A, PARSED, ?> holder;
    private final Wrapper wrapper;
    private final Meta meta = Meta.create();

    ContextRequirementImpl(ContextRegistry<SENDER> contextRegistry, AnnotationHolder<A, PARSED, ?> holder, Wrapper wrapper) {
        this.contextRegistry = contextRegistry;
        this.holder = holder;
        this.wrapper = wrapper;
    }

    @Override
    public String getName() {
        return this.holder.getName();
    }

    @Override
    public AnnotationHolder<?, PARSED, ?> getAnnotationHolder() {
        return holder;
    }

    @Override
    public <CONTEXT extends ParseableInputMatcher<CONTEXT>> RequirementResult<PARSED> match(Invocation<SENDER> invocation, CONTEXT matcher) {
        WrapFormat<PARSED, ?> wrapFormat = holder.getFormat();
        ContextResult<PARSED> result = contextRegistry.provideContext(wrapFormat.getParsedType(), invocation);

        if (result.hasResult()) {
            return RequirementResult.success(wrapper.create(result.getResult(), wrapFormat));
        }

        return RequirementResult.failure(result.getError());
    }

    @Override
    public Meta meta() {
        return meta;
    }

    @Override
    public @Nullable MetaHolder parentMeta() {
        return null;
    }
}
