package dev.rollczi.litecommands.command.executor;

import dev.rollczi.litecommands.handler.exception.ExceptionHandleService;
import dev.rollczi.litecommands.invocation.Invocation;

import java.util.concurrent.CompletionException;
import java.util.function.Function;

public class LastExceptionHandler<SENDER> implements Function<Throwable, CommandExecuteResult> {

    private final ExceptionHandleService<SENDER> exceptionHandleService;
    private final Invocation<SENDER> invocation;

    public LastExceptionHandler(ExceptionHandleService<SENDER> exceptionHandleService, Invocation<SENDER> invocation) {
        this.exceptionHandleService = exceptionHandleService;
        this.invocation = invocation;
    }

    @Override
    public CommandExecuteResult apply(Throwable throwable) {
        if (throwable instanceof CompletionException) {
            throwable = throwable.getCause();
        }

        try {
            this.exceptionHandleService.resolve(invocation, throwable);
        }
        catch (Throwable lastError) { // Handle exception from exception handler
            lastError.printStackTrace();
        }

        return CommandExecuteResult.thrown(null, throwable);
    }

}
