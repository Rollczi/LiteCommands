package dev.rollczi.litecommands.context;

import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.meta.MetaHolder;
import dev.rollczi.litecommands.reflect.type.TypeToken;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

class ContextRequirementImpl<T> implements ContextRequirement<T> {

    private final Supplier<String> name;
    private final TypeToken<T> format;
    private final Meta meta = Meta.create();

    ContextRequirementImpl(Supplier<String> name, TypeToken<T> format) {
        this.name = name;
        this.format = format;
    }

    @Override
    public String getName() {
        return name.get();
    }

    @Override
    public TypeToken<T> getType() {
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
