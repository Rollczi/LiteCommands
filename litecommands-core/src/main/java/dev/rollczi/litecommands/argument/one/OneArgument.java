package dev.rollczi.litecommands.argument.one;

import dev.rollczi.litecommands.command.Suggestion;
import dev.rollczi.litecommands.command.LiteInvocation;
import panda.std.Result;

import java.util.Collections;
import java.util.List;

@FunctionalInterface
public interface OneArgument<T> {

    Result<T, Object> parse(LiteInvocation invocation, String argument);

    default List<Suggestion> suggest(LiteInvocation invocation) {
        return Collections.emptyList();
    }

}
