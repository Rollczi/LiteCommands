package dev.rollczi.litecommands.flag;

import dev.rollczi.litecommands.argument.ArgumentKey;
import dev.rollczi.litecommands.argument.SimpleArgument;
import dev.rollczi.litecommands.wrapper.WrapFormat;

import java.util.Optional;
import java.util.function.Supplier;

public class FlagArgument extends SimpleArgument<Boolean> {

    public static final ArgumentKey KEY = ArgumentKey.typed(FlagArgument.class);

    public FlagArgument(String name, WrapFormat<Boolean, ?> parsedType) {
        super(name, parsedType);
    }

    @Override
    public Optional<Boolean> defaultValue() {
        return Optional.of(false);
    }

}
