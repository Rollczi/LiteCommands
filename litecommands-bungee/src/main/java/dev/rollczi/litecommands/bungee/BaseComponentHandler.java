package dev.rollczi.litecommands.bungee;

import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.handle.Handler;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.function.Function;

class BaseComponentHandler implements Handler<CommandSender, BaseComponent> {

    @Override
    public void handle(CommandSender sender, LiteInvocation invocation, BaseComponent value) {
        sender.sendMessage(value);
    }

}
