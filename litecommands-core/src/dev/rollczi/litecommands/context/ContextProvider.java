package dev.rollczi.litecommands.context;

import dev.rollczi.litecommands.invocation.Invocation;
import panda.std.Result;

public interface ContextProvider<SENDER, T> {

    Result<T, Object> provide(Invocation<SENDER> invocation);

}
