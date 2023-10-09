package dev.rollczi.litecommands.handler.exception.standard;

import dev.rollczi.litecommands.handler.exception.ExceptionHandler;
import dev.rollczi.litecommands.handler.result.ResultHandlerChain;
import dev.rollczi.litecommands.invocation.Invocation;

import java.lang.reflect.InvocationTargetException;

public class InvocationTargetExceptionHandler<SENDER> implements ExceptionHandler<SENDER, InvocationTargetException> {

    @Override
    public void handle(Invocation<SENDER> invocation, InvocationTargetException exception, ResultHandlerChain<SENDER> chain) {
        Throwable cause = exception.getCause();

        if (cause == null || cause.equals(exception)) {
            chain.resolve(invocation, exception, ReflectiveOperationException.class);
            return;
        }

        chain.resolve(invocation, cause);
    }

}
