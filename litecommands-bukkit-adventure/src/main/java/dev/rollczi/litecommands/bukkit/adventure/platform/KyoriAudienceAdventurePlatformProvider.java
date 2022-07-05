package dev.rollczi.litecommands.bukkit.adventure.platform;

import dev.rollczi.litecommands.bukkit.adventure.KyoriAudienceProvider;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.AudienceProvider;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.entity.Player;

class KyoriAudienceAdventurePlatformProvider implements KyoriAudienceProvider {

    private final AudienceProvider audienceProvider;

    public KyoriAudienceAdventurePlatformProvider(AudienceProvider audienceProvider) {
        this.audienceProvider = audienceProvider;
    }

    @Override
    public Audience sender(CommandSender commandSender) {
        if (commandSender instanceof Audience) {
            return (Audience) commandSender;
        }

        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            return audienceProvider.player(player.getUniqueId());
        }

        if (commandSender instanceof ConsoleCommandSender || commandSender instanceof RemoteConsoleCommandSender) {
            return audienceProvider.console();
        }

        throw new IllegalArgumentException("Unsupported command sender type: " + commandSender.getClass().getName());
    }

}
