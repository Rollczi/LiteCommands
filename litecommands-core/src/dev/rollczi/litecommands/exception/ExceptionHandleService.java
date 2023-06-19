package dev.rollczi.litecommands.exception;

import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.util.MapUtil;

import java.util.HashMap;
import java.util.Map;

public class ExceptionHandleService<SENDER> {

    private ExceptionHandler<SENDER, Throwable> unexpectedHandler = (invocation, throwable) -> throwable.printStackTrace();
    private final Map<Class<?>, ExceptionHandler<SENDER, ?>> handlers = new HashMap<>();

    public void registerHandler(Class<? extends Throwable> exceptionType, ExceptionHandler<SENDER, ? extends Throwable> handler) {
        this.handlers.put(exceptionType, handler);
    }

    public void registerUnexpectedHandler(ExceptionHandler<SENDER, Throwable> handler) {
        this.unexpectedHandler = handler;
    }

    @SuppressWarnings("unchecked")
    public <E extends Throwable> void resolve(Invocation<SENDER> invocation, E exception) {
        ExceptionHandler<SENDER, ? super E> handler = (ExceptionHandler<SENDER, ? super E>) MapUtil.findBySuperTypeOf(exception.getClass(), this.handlers)
            .orElse(null);

        if (handler == null) {
            this.unexpectedHandler.handle(invocation, exception);
            return;
        }

        handler.handle(invocation, exception);
    }

}
