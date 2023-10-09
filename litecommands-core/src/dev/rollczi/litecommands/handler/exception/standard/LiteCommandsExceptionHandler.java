package dev.rollczi.litecommands.handler.exception.standard;

import dev.rollczi.litecommands.LiteCommandsException;
import dev.rollczi.litecommands.handler.exception.ExceptionHandler;
import dev.rollczi.litecommands.handler.result.ResultHandlerChain;
import dev.rollczi.litecommands.invocation.Invocation;

public class LiteCommandsExceptionHandler<SENDER> implements ExceptionHandler<SENDER, LiteCommandsException> {

    @Override
    public void handle(Invocation<SENDER> invocation, LiteCommandsException exception, ResultHandlerChain<SENDER> chain) {
        Throwable cause = exception.getCause();

        if (cause == null || cause.equals(exception)) {
            chain.resolve(invocation, exception, RuntimeException.class);
            return;
        }

        chain.resolve(invocation, cause);
    }

}
