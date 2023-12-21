package dev.rollczi.litecommands.argument.resolver.collector;

import dev.rollczi.litecommands.argument.ArgumentKey;
import dev.rollczi.litecommands.argument.SimpleArgument;
import dev.rollczi.litecommands.reflect.type.TypeToken;
import dev.rollczi.litecommands.wrapper.WrapFormat;

public class CollectorArgument<R> extends SimpleArgument<R> {

    public static final ArgumentKey KEY = ArgumentKey.typed(CollectorArgument.class);

    private final TypeToken<?> elementType;

    public CollectorArgument(String name, WrapFormat<R, ?> wrapperFormat, TypeToken<?> elementType) {
        super(name, wrapperFormat, false);
        this.elementType = elementType;
    }

    public TypeToken<?> getElementTypeToken() {
        return elementType;
    }

}
