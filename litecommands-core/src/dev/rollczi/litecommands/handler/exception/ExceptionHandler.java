package dev.rollczi.litecommands.handler.exception;

import dev.rollczi.litecommands.handler.result.ResultHandler;
import dev.rollczi.litecommands.handler.result.ResultHandlerChain;
import dev.rollczi.litecommands.invocation.Invocation;

@FunctionalInterface
public interface ExceptionHandler<SENDER, E extends Throwable> extends ResultHandler<SENDER, E> {

    void handle(Invocation<SENDER> invocation, E exception, ResultHandlerChain<SENDER> chain);

}
