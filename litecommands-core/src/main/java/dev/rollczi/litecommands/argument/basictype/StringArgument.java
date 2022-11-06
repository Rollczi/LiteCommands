package dev.rollczi.litecommands.argument.basictype;

import java.util.function.Function;

public class StringArgument extends AbstractBasicTypeArgument<String> {

    public StringArgument() {
        super(Function.identity(), () -> TypeUtils.STRING_SUGGESTION);
    }

}
