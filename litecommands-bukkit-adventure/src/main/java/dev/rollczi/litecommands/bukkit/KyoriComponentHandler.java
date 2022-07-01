package dev.rollczi.litecommands.bukkit;

import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.handle.Handler;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.AudienceProvider;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.entity.Player;

class KyoriComponentHandler implements Handler<CommandSender, Component> {

    private final AudienceProvider audienceProvider;

    KyoriComponentHandler(AudienceProvider audienceProvider) {
        this.audienceProvider = audienceProvider;
    }

    @Override
    public void handle(CommandSender commandSource, LiteInvocation invocation, Component value) {
        if (commandSource instanceof Audience) {
            Audience audience = (Audience) commandSource;

            audience.sendMessage(value);
        }

        if (commandSource instanceof Player) {
            Player player = (Player) commandSource;
            Audience audience = audienceProvider.player(player.getUniqueId());

            audience.sendMessage(value);
        }

        if (commandSource instanceof ConsoleCommandSender || commandSource instanceof RemoteConsoleCommandSender) {
            Audience console = audienceProvider.console();

            console.sendMessage(value);
        }
    }

}
