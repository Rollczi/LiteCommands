package dev.rollczi.litecommands.context;

import dev.rollczi.litecommands.invocation.Invocation;

public interface ContextProvider<SENDER, T> {

    ContextResult<T> provide(Invocation<SENDER> invocation);

}
