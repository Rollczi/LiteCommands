package dev.rollczi.litecommands.bungee;

import dev.rollczi.litecommands.command.CommandExecuteResultHandler;
import dev.rollczi.litecommands.invocation.Invocation;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;

class BaseComponentHandler implements CommandExecuteResultHandler<CommandSender, BaseComponent> {

    @Override
    public void handle(Invocation<CommandSender> invocation, BaseComponent result) {
        invocation.getSender().sendMessage(result);
    }

}
