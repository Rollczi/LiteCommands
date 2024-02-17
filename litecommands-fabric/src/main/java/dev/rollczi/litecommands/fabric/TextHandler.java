package dev.rollczi.litecommands.fabric;

import dev.rollczi.litecommands.handler.result.ResultHandler;
import dev.rollczi.litecommands.handler.result.ResultHandlerChain;
import dev.rollczi.litecommands.invocation.Invocation;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

class TextHandler implements ResultHandler<ServerCommandSource, Text> {
    @Override
    public void handle(Invocation<ServerCommandSource> invocation, Text result, ResultHandlerChain<ServerCommandSource> chain) {
        invocation.sender().sendFeedback(() -> result, false);
    }
}
