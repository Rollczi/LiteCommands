package dev.rollczi.litecommands.handler.result;

import dev.rollczi.litecommands.invocation.Invocation;

public interface ResultHandlerChain<SENDER> {

    <T> void resolve(Invocation<SENDER> invocation, T result);

}
