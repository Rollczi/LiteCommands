package dev.rollczi.litecommands.invalidusage;

import dev.rollczi.litecommands.handler.exception.ExceptionHandler;
import dev.rollczi.litecommands.handler.result.ResultHandlerChain;
import dev.rollczi.litecommands.invocation.Invocation;

public class InvalidUsageExceptionHandler<SENDER> implements ExceptionHandler<SENDER, InvalidUsageException> {

    @Override
    public void handle(Invocation<SENDER> invocation, InvalidUsageException exception, ResultHandlerChain<SENDER> chain) {
        chain.resolve(invocation, exception.getErrorResult());
    }

}
