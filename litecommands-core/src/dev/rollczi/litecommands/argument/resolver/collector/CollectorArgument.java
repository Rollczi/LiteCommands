package dev.rollczi.litecommands.argument.resolver.collector;

import dev.rollczi.litecommands.argument.ArgumentKey;
import dev.rollczi.litecommands.argument.SimpleArgument;
import dev.rollczi.litecommands.reflect.type.TypeToken;
import dev.rollczi.litecommands.wrapper.WrapFormat;

public class CollectorArgument<R> extends SimpleArgument<R> {

    public static final ArgumentKey KEY = ArgumentKey.typed(CollectorArgument.class);

    private final TypeToken<?> elementType;
    private final String delimiter;

    public CollectorArgument(String name, WrapFormat<R, ?> wrapperFormat, TypeToken<?> elementType, String delimiter) {
        super(name, wrapperFormat, false);
        this.elementType = elementType;
        this.delimiter = delimiter;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public TypeToken<?> getElementTypeToken() {
        return elementType;
    }

}
