package dev.rollczi.litecommands.valid.handle;

import dev.rollczi.litecommands.LiteInvocation;
import dev.rollczi.litecommands.valid.ValidationCommandException;

public interface ValidationExceptionHandler {

    void handle(LiteInvocation invocation, ValidationCommandException exception);

}
