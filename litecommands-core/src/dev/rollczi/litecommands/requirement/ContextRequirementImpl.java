package dev.rollczi.litecommands.requirement;

import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.meta.MetaHolder;
import dev.rollczi.litecommands.wrapper.WrapFormat;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

class ContextRequirementImpl<T> implements ContextRequirement<T> {

    private final Supplier<String> name;
    private final WrapFormat<T, ?> format;
    private final Meta meta = Meta.create();

    ContextRequirementImpl(Supplier<String> name, WrapFormat<T, ?> format) {
        this.name = name;
        this.format = format;
    }


    @Override
    public String getName() {
        return name.get();
    }

    @Override
    public WrapFormat<T, ?> getWrapperFormat() {
        return format;
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
