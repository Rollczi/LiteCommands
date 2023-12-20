package dev.rollczi.litecommands.argument.resolver.array;

import dev.rollczi.litecommands.argument.ArgumentKey;
import dev.rollczi.litecommands.argument.SimpleArgument;
import dev.rollczi.litecommands.wrapper.WrapFormat;

public class ArrayArgument extends SimpleArgument<Object> {

    public static final ArgumentKey KEY = ArgumentKey.typed(ArrayArgument.class);

    private final Class<?> componentType;
    private final Class<?> arrayType;

    public ArrayArgument(String name, WrapFormat<Object, ?> wrapperFormat, Class<?> componentType, Class<?> arrayType) {
        super(name, wrapperFormat, false);
        this.componentType = componentType;
        this.arrayType = arrayType;
    }

}
