package dev.rollczi.litecommands.bungee;

import dev.rollczi.litecommands.handler.result.ResultHandler;
import dev.rollczi.litecommands.invocation.Invocation;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;

class BaseComponentHandler implements ResultHandler<CommandSender, BaseComponent> {

    @Override
    public void handle(Invocation<CommandSender> invocation, BaseComponent result) {
        invocation.sender().sendMessage(result);
    }

}
