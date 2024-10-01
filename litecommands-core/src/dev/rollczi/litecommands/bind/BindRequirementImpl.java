package dev.rollczi.litecommands.bind;

import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.meta.MetaHolder;
import dev.rollczi.litecommands.reflect.type.TypeToken;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

class BindRequirementImpl<T> implements BindRequirement<T> {

    private final Supplier<String> name;
    private final TypeToken<T> typeToken;
    private final Meta meta = Meta.create();

    BindRequirementImpl(Supplier<String> name, TypeToken<T> typeToken) {
        this.name = name;
        this.typeToken = typeToken;
    }

    @Override
    public String getName() {
        return name.get();
    }

    @Override
    public TypeToken<T> getType() {
        return typeToken;
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
