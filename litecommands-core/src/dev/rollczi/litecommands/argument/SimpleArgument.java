package dev.rollczi.litecommands.argument;

import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.meta.MetaHolder;
import dev.rollczi.litecommands.wrapper.WrapFormat;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class SimpleArgument<T> implements Argument<T> {

    private final Supplier<String> name;
    private final WrapFormat<T, ?> wrapperFormat;
    private final Meta meta = Meta.create();

    public SimpleArgument(Supplier<String> name, WrapFormat<T, ?> wrapperFormat) {
        this.name = name;
        this.wrapperFormat = wrapperFormat;
    }

    @Override
    public String getName() {
        return name.get();
    }

    @Override
    public WrapFormat<T, ?> getWrapperFormat() {
        return this.wrapperFormat;
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
