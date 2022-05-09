package dev.rollczi.litecommands.velocity;

import com.velocitypowered.api.command.CommandSource;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.handle.Handler;
import net.kyori.adventure.text.Component;

class KyoriComponentHandler implements Handler<CommandSource, Component> {

    @Override
    public void handle(CommandSource commandSource, LiteInvocation invocation, Component value) {
        commandSource.sendMessage(value);
    }

}
