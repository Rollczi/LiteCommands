package dev.rollczi.litecommands.handler.exception.standard;

import dev.rollczi.litecommands.handler.exception.ExceptionHandler;
import dev.rollczi.litecommands.handler.result.ResultHandler;
import dev.rollczi.litecommands.handler.result.ResultHandlerChain;
import dev.rollczi.litecommands.invocation.Invocation;

public class ThrowableHandler<SENDER> implements ExceptionHandler<SENDER, Throwable> {

    @Override
    public void handle(Invocation<SENDER> invocation, Throwable throwable, ResultHandlerChain<SENDER> chain) {
        throwable.printStackTrace(); //TODO: logger
    }

}
