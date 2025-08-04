package dev.rollczi.litecommands.unit;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.SimpleArgument;
import dev.rollczi.litecommands.reflect.type.TypeToken;

public class Arguments {

    public static <T> Argument<T> argument() {
        return new SimpleArgument<>("test", new TypeToken<T>(){});
    }

}
