package dev.rollczi.litecommands.inject;

import dev.rollczi.litecommands.valid.ValidationCommandException;
import panda.std.Option;

import java.util.List;

public interface ArgumentHandler<T> {

    T parse(InjectContext context, int rawIndex) throws ValidationCommandException;

    List<String> tabulation(String command, String[] args);

    default Option<String> getName() {
        return Option.of(this.getClass().getAnnotation(ArgumentName.class))
                .map(ArgumentName::value);
    }

}
