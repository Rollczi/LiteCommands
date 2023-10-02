package dev.rollczi.litecommands.handler.result.basic;

import dev.rollczi.litecommands.handler.result.ResultHandler;
import dev.rollczi.litecommands.handler.result.ResultHandlerChain;
import dev.rollczi.litecommands.invocation.Invocation;

public class ThrowableHandler<SENDER> implements ResultHandler<SENDER, Throwable> {

    @Override
    public void handle(Invocation<SENDER> invocation, Throwable result, ResultHandlerChain<SENDER> chain) {
        result.printStackTrace(); //TODO: logger
    }

}
