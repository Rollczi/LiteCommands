package dev.rollczi.litecommands.handler.result.basic;

import dev.rollczi.litecommands.handler.result.ResultHandler;
import dev.rollczi.litecommands.handler.result.ResultHandlerChain;
import dev.rollczi.litecommands.invocation.Invocation;

import java.util.concurrent.CompletionStage;

public class CompletionStageHandler<SENDER> implements ResultHandler<SENDER, CompletionStage> {

    @Override
    public void handle(Invocation<SENDER> invocation, CompletionStage result, ResultHandlerChain<SENDER> chain) {
        result.whenComplete((value, throwable) -> {
            if (throwable != null) {
                chain.resolve(invocation, throwable);
            } else {
                chain.resolve(invocation, value);
            }
        });
    }

}
