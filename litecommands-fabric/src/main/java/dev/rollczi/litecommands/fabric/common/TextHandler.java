package dev.rollczi.litecommands.fabric.common;

import dev.rollczi.litecommands.handler.result.ResultHandler;
import dev.rollczi.litecommands.handler.result.ResultHandlerChain;
import dev.rollczi.litecommands.invocation.Invocation;
import net.minecraft.text.Text;

import java.util.function.BiConsumer;

public class TextHandler<SOURCE> implements ResultHandler<SOURCE, Text> {
    BiConsumer<SOURCE, Text> func;

    public TextHandler(BiConsumer<SOURCE, Text> sendFeedback) {
        this.func = sendFeedback;
    }

    @Override
    public void handle(Invocation<SOURCE> invocation, Text result, ResultHandlerChain<SOURCE> chain) {
        func.accept(invocation.sender(), result);
    }
}
