package dev.rollczi.litecommands.contextual;

import dev.rollczi.litecommands.command.LiteInvocation;
import panda.std.Result;

@FunctionalInterface
public interface Contextual<SENDER, T> {

    Result<T, Object> extract(SENDER sender, LiteInvocation invocation);

}
