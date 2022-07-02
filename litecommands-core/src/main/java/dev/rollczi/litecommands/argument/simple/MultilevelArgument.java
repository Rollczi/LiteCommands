package dev.rollczi.litecommands.argument.simple;

import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.sugesstion.Suggestion;
import panda.std.Result;

import java.util.Collections;
import java.util.List;

public interface MultilevelArgument<T> {

    Result<T, ?> parseMultilevel(LiteInvocation invocation, String... arguments);

    default List<Suggestion> suggest(LiteInvocation invocation) {
        return Collections.emptyList();
    }

    default boolean validate(LiteInvocation invocation, Suggestion suggestion) {
        return false;
    }

    int countMultilevel();

}
