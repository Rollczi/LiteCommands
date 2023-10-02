package dev.rollczi.litecommands.handler.result.basic;

import dev.rollczi.litecommands.handler.result.ResultHandler;
import dev.rollczi.litecommands.handler.result.ResultHandlerChain;
import dev.rollczi.litecommands.invocation.Invocation;
import panda.std.Option;

public class OptionHandler<SENDER> implements ResultHandler<SENDER, Option> {

    @Override
    public void handle(Invocation<SENDER> invocation, Option result, ResultHandlerChain<SENDER> chain) {
        if (result.isPresent()) {
            chain.resolve(invocation, result.get());
        }
    }

}
