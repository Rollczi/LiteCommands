package dev.rollczi.litecommands.fabric.common;

import dev.rollczi.litecommands.handler.result.ResultHandler;
import dev.rollczi.litecommands.handler.result.ResultHandlerChain;
import dev.rollczi.litecommands.invocation.Invocation;

import java.util.function.BiConsumer;

public class StringHandler<SOURCE> implements ResultHandler<SOURCE, String> {
    BiConsumer<SOURCE, String> func;

    public StringHandler(BiConsumer<SOURCE, String> sendFeedback) {
        this.func = sendFeedback;
    }

    @Override
    public void handle(Invocation<SOURCE> invocation, String result, ResultHandlerChain<SOURCE> chain) {
        this.func.accept(invocation.sender(), result);
    }
}
