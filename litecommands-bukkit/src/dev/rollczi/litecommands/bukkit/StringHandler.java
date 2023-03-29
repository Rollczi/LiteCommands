package dev.rollczi.litecommands.bukkit;

import dev.rollczi.litecommands.command.CommandExecuteResultHandler;
import dev.rollczi.litecommands.invocation.Invocation;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.function.UnaryOperator;

class StringHandler implements CommandExecuteResultHandler<CommandSender, String> {

    static final UnaryOperator<String> DESERIALIZE_AMPERSAND = text -> ChatColor.translateAlternateColorCodes('&', text);

    @Override
    public void handle(Invocation<CommandSender> invocation, String result) {
        invocation.getSender().sendMessage(DESERIALIZE_AMPERSAND.apply(result));
    }

}
