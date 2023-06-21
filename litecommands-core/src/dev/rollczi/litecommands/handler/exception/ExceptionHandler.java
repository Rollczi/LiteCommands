package dev.rollczi.litecommands.handler.exception;

import dev.rollczi.litecommands.invocation.Invocation;

public interface ExceptionHandler<SENDER, E extends Throwable> {

    void handle(Invocation<SENDER> invocation, E exception);

}
