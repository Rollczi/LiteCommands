package dev.rollczi.litecommands.handler.result;

import dev.rollczi.litecommands.invocation.Invocation;

public interface ResultHandlerChain<SENDER> {

    @SuppressWarnings("unchecked")
    default <T> void resolve(Invocation<SENDER> invocation, T result) {
        resolve(invocation, result, (Class<T>) result.getClass());
    }

    <T> void resolve(Invocation<SENDER> invocation, T result, Class<? super T> typeAs);

}
