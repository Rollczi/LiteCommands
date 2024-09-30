package dev.rollczi.litecommands.bukkit.argument;

import com.google.common.collect.Sets;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.bukkit.LiteBukkitMessages;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.message.MessageRegistry;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import java.time.Instant;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class OfflinePlayerArgument extends ArgumentResolver<CommandSender, OfflinePlayer> {

    private final Server server;
    private final Plugin plugin;
    private final boolean enableThreadCheck;

    private final Set<String> nicknames = Sets.newConcurrentHashSet();
    private Instant nextWarningDate = Instant.EPOCH;

    public OfflinePlayerArgument(Server server, Plugin plugin, boolean enableThreadCheck) {
        this.server = server;
        this.plugin = plugin;
        this.enableThreadCheck = enableThreadCheck;

        // Server#getOfflinePlayers() can be blocking, so we don't want to call it every time
        nicknames.addAll(
            Arrays.stream(server.getOfflinePlayers())
                .map(OfflinePlayer::getName)
                .collect(Collectors.toList())
        );

        // Save new joining player names so our suggestions are more wide
        server.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onPlayerJoin(PlayerJoinEvent event) {
                nicknames.add(event.getPlayer().getName());
            }
        }, plugin);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected ParseResult<OfflinePlayer> parse(Invocation<CommandSender> invocation, Argument<OfflinePlayer> context, String argument) {
        if (enableThreadCheck && server.isPrimaryThread() && Instant.now().isAfter(nextWarningDate)) {
            plugin.getLogger().warning("LiteCommands | OfflinePlayer argument resolved synchronously! This might cause server freeze!");
            nextWarningDate = Instant.now().plusSeconds(60);
        }

        return ParseResult.success(server.getOfflinePlayer(argument));
    }

    @Override
    public SuggestionResult suggest(Invocation<CommandSender> invocation, Argument<OfflinePlayer> argument, SuggestionContext context) {
        return SuggestionResult.of(nicknames);
    }

}
