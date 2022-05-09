package dev.rollczi.litecommands.bukkit;

import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.handle.Handler;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.function.Function;

class StringHandler implements Handler<CommandSender, String> {

    private static final Function<String, String> DESERIALIZE_AMPERSAND = text -> ChatColor.translateAlternateColorCodes('&', text);

    @Override
    public void handle(CommandSender sender, LiteInvocation invocation, String value) {
        sender.sendMessage(DESERIALIZE_AMPERSAND.apply(value));
    }

}
