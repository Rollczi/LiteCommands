package dev.rollczi.litecommands.bungee;

import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.handle.Handler;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;

class BaseComponentHandler implements Handler<CommandSender, BaseComponent> {

    @Override
    public void handle(CommandSender sender, LiteInvocation invocation, BaseComponent value) {
        sender.sendMessage(value);
    }

}
