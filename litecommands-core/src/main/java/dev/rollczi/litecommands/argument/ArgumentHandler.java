package dev.rollczi.litecommands.argument;

import dev.rollczi.litecommands.component.LiteComponent;
import dev.rollczi.litecommands.valid.ValidationCommandException;
import panda.std.Option;

import java.util.List;

public interface ArgumentHandler<T> {

    T parse(LiteComponent.ContextOfResolving context, int rawIndex) throws ValidationCommandException;

    List<String> tabulation(LiteComponent.ContextOfResolving context);

    default Option<String> getName() {
        return Option.of(this.getClass().getAnnotation(ArgumentName.class))
                .map(ArgumentName::value);
    }

}
