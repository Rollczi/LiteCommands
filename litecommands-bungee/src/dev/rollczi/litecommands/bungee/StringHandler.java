package dev.rollczi.litecommands.bungee;

import dev.rollczi.litecommands.command.CommandExecuteResultHandler;
import dev.rollczi.litecommands.invocation.Invocation;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.function.UnaryOperator;

class StringHandler implements CommandExecuteResultHandler<CommandSender, String> {

    static final UnaryOperator<String> DESERIALIZE_AMPERSAND = text -> ChatColor.translateAlternateColorCodes('&', text);

    @Override
    public void handle(Invocation<CommandSender> invocation, String result) {
        invocation.sender().sendMessage(TextComponent.fromLegacyText(DESERIALIZE_AMPERSAND.apply(result)));
    }

}
