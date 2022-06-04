package dev.rollczi.litecommands.bungee;

import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.handle.Handler;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.function.Function;

class StringHandler implements Handler<CommandSender, String> {

    private static final Function<String, String> DESERIALIZE_AMPERSAND = text -> ChatColor.translateAlternateColorCodes('&', text);

    @Override
    public void handle(CommandSender sender, LiteInvocation invocation, String value) {
        sender.sendMessage(TextComponent.fromLegacyText(DESERIALIZE_AMPERSAND.apply(value)));
    }

}
