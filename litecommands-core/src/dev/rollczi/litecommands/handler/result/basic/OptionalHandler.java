package dev.rollczi.litecommands.handler.result.basic;

import dev.rollczi.litecommands.handler.result.ResultHandler;
import dev.rollczi.litecommands.handler.result.ResultHandlerChain;
import dev.rollczi.litecommands.invocation.Invocation;

import java.util.Optional;

public class OptionalHandler<SENDER> implements ResultHandler<SENDER, Optional> {

    @Override
    public void handle(Invocation<SENDER> invocation, Optional result, ResultHandlerChain<SENDER> chain) {
        if (result.isPresent()) {
            chain.resolve(invocation, result.get());
        }
    }

}
