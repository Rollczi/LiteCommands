package dev.rollczi.litecommands.command.executor;

import dev.rollczi.litecommands.handler.result.ResultHandleService;
import dev.rollczi.litecommands.invocation.Invocation;

import java.util.concurrent.CompletionException;
import java.util.function.Function;

public class LastExceptionHandler<SENDER> implements Function<Throwable, CommandExecuteResult> {

    private final ResultHandleService<SENDER> resultHandleService;
    private final Invocation<SENDER> invocation;

    public LastExceptionHandler(ResultHandleService<SENDER> resultHandleService, Invocation<SENDER> invocation) {
        this.resultHandleService = resultHandleService;
        this.invocation = invocation;
    }

    @Override
    public CommandExecuteResult apply(Throwable throwable) {
        if (throwable instanceof CompletionException) {
            throwable = throwable.getCause();
        }

        try {
            this.resultHandleService.resolve(invocation, throwable);
        }
        catch (Throwable lastError) { // Handle exception from exception handler
            lastError.printStackTrace();
        }

        return CommandExecuteResult.thrown(null, throwable);
    }

}
