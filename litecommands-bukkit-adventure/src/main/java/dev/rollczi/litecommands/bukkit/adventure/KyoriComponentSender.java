package dev.rollczi.litecommands.bukkit.adventure;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.AudienceProvider;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.entity.Player;

class KyoriComponentSender {

    private final AudienceProvider audienceProvider;

    KyoriComponentSender(AudienceProvider audienceProvider) {
        this.audienceProvider = audienceProvider;
    }

    void send(CommandSender commandSender, Component message) {
        if (commandSender instanceof Audience) {
            Audience audience = (Audience) commandSender;

            audience.sendMessage(message);
        }

        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            Audience audience = audienceProvider.player(player.getUniqueId());

            audience.sendMessage(message);
        }

        if (commandSender instanceof ConsoleCommandSender || commandSender instanceof RemoteConsoleCommandSender) {
            Audience console = audienceProvider.console();

            console.sendMessage(message);
        }
    }

}
