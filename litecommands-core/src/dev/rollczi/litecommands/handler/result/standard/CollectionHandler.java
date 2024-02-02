package dev.rollczi.litecommands.handler.result.standard;

import dev.rollczi.litecommands.handler.result.ResultHandler;
import dev.rollczi.litecommands.handler.result.ResultHandlerChain;
import dev.rollczi.litecommands.invocation.Invocation;

import java.util.Collection;
import java.util.concurrent.CompletionStage;

public class CollectionHandler<SENDER> implements ResultHandler<SENDER, Collection> {

    @Override
    public void handle(Invocation<SENDER> invocation, Collection result, ResultHandlerChain<SENDER> chain) {
        for (Object object : result) {
            chain.resolve(invocation, object);
        }
    }
}
