package dev.rollczi.litecommands.context;

import dev.rollczi.litecommands.invocation.Invocation;

public interface ContextProvider<SENDER, T> extends ContextChainedProvider<SENDER, T> {

    ContextResult<T> provide(Invocation<SENDER> invocation);

    @Override
    default ContextResult<T> provide(Invocation<SENDER> invocation, ContextChainAccessor<SENDER> accessor) {
        return provide(invocation);
    }

}
