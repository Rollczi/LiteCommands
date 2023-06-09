package dev.rollczi.litecommands.adventure.bukkit.platform;

import dev.rollczi.litecommands.adventure.AdventureAudienceProvider;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.AudienceProvider;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.entity.Player;
import panda.std.Option;
import panda.std.stream.PandaStream;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class BukkitAdventurePlatformAudienceProvider implements AdventureAudienceProvider<CommandSender> {

    private final AudienceProvider audienceProvider;

    public BukkitAdventurePlatformAudienceProvider(AudienceProvider audienceProvider) {
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
