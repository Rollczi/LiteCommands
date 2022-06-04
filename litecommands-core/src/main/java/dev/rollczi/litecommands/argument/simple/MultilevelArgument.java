package dev.rollczi.litecommands.argument.simple;

import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.command.sugesstion.Suggestion;
import panda.std.Result;

import java.util.Collections;
import java.util.List;

public interface MultilevelArgument<T> {

    Result<T, ?> parseMultilevel(LiteInvocation invocation, String... arguments);

    default List<Suggestion> suggest(LiteInvocation invocation) {
        return Collections.emptyList();
    }

    int countMultilevel();

}
