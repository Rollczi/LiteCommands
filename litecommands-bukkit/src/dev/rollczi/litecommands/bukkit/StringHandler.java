package dev.rollczi.litecommands.bukkit;

import dev.rollczi.litecommands.result.ResultHandler;
import dev.rollczi.litecommands.invocation.Invocation;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.function.UnaryOperator;

class StringHandler implements ResultHandler<CommandSender, String> {

    static final UnaryOperator<String> DESERIALIZE_AMPERSAND = text -> ChatColor.translateAlternateColorCodes('&', text);

    @Override
    public void handle(Invocation<CommandSender> invocation, String result) {
        invocation.sender().sendMessage(DESERIALIZE_AMPERSAND.apply(result));
    }

}
