package dev.rollczi.litecommands.argument;

import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.meta.Meta;
import dev.rollczi.litecommands.meta.MetaHolder;
import dev.rollczi.litecommands.wrapper.WrapFormat;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class SimpleArgument<T> implements Argument<T> {

    private final String name;
    private final WrapFormat<T, ?> wrapperFormat;
    private final Meta meta = Meta.create();
    private final boolean nullable;
    // cache last argument
    private String lastKeyName;
    private ArgumentKey lastKey;

    public SimpleArgument(String name, WrapFormat<T, ?> wrapperFormat, boolean nullable) {
        this.name = name;
        this.wrapperFormat = wrapperFormat;
        this.nullable = nullable;
    }

    public SimpleArgument(String name, WrapFormat<T, ?> wrapperFormat) {
        this.name = name;
        this.wrapperFormat = wrapperFormat;
        this.nullable = false;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ArgumentKey getKey() {
        String keyName = this.getKeyName();
        if (lastKeyName == null || !lastKeyName.equals(keyName)) {
            lastKey = Argument.super.getKey();
            lastKeyName = keyName;
        }

        return lastKey;
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

    @Override
    public Optional<ParseResult<T>> defaultValue() {
        if (nullable) {
            return Optional.of(ParseResult.successNull());
        }

        return Optional.empty();
    }

}
