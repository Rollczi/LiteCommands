package dev.rollczi.litecommands.handler.result;

import dev.rollczi.litecommands.invocation.Invocation;

public interface ResultHandleService<SENDER> {

    <T> void registerHandler(Class<T> resultType, ResultHandler<SENDER, ? extends T> handler);

    <T> void resolve(Invocation<SENDER> invocation, T result);

    <T> void resolve(Invocation<SENDER> invocation, T result, Class<? super T> typedAs, ResultHandlerChain<SENDER> chain);

}
