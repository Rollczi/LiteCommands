package dev.rollczi.litecommands.annotations.context;

import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.meta.MetaHolder;
import dev.rollczi.litecommands.annotations.AnnotationHolder;
import dev.rollczi.litecommands.requirement.ContextRequirement;
import dev.rollczi.litecommands.wrapper.WrapFormat;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;

class ContextRequirementImpl<PARSED, A extends Annotation> implements ContextRequirement<PARSED> {

    private final AnnotationHolder<A, PARSED, ?> holder;
    private final Meta meta = Meta.create();

    ContextRequirementImpl(AnnotationHolder<A, PARSED, ?> holder) {
        this.holder = holder;
    }

    @Override
    public String getName() {
        return this.holder.getName();
    }

    @Override
    public WrapFormat<PARSED, ?> getWrapperFormat() {
        return holder.getFormat();
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
