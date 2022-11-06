package dev.rollczi.litecommands.argument.basictype;

import panda.std.Option;

public class BooleanArgument extends AbstractBasicTypeArgument<Boolean> {

    public BooleanArgument() {
        super(BooleanArgument::parse, () -> TypeUtils.BOOLEAN_SUGGESTION);
    }

    private static Boolean parse(String argument) {
        return Option.of(argument)
            .filter(arg -> arg.equalsIgnoreCase("true") || arg.equalsIgnoreCase("false"))
            .map(Boolean::parseBoolean)
            .orThrow(() -> new IllegalArgumentException("Argument is not boolean"));
    }

}
