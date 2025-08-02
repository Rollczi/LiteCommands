package dev.rollczi.litecommands.bukkit.argument;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.bukkit.LiteBukkitMessages;
import dev.rollczi.litecommands.bukkit.LiteBukkitSettings;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.message.MessageRegistry;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import java.util.TreeSet;
import java.util.regex.Pattern;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.plugin.Plugin;

public class OfflinePlayerArgument extends ArgumentResolver<CommandSender, OfflinePlayer> {

    private static final int SUGGESTION_LIMIT = 256;

    private final Server server;
    private final MessageRegistry<CommandSender> messageRegistry;
    private final boolean allowParseUnknownPlayers;
    private final Pattern playerNamePattern;
    private final TreeSet<String> nicknames = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);

    public OfflinePlayerArgument(Server server, Plugin plugin, MessageRegistry<CommandSender> messageRegistry, boolean allowParseUnknownPlayers, Pattern playerNamePattern) {
        this.server = server;
        this.messageRegistry = messageRegistry;
        this.allowParseUnknownPlayers = allowParseUnknownPlayers;
        this.playerNamePattern = playerNamePattern;

        // Server#getOfflinePlayers() can be blocking, so we don't want to call it every time
        server.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onServerStart(ServerLoadEvent event) {
                nicknames.clear();
                for (OfflinePlayer player : server.getOfflinePlayers()) {
                    final String name = player.getName();
                    // According to Bukkit API documentation, offline player objects may have no name
                    //  if the server only knows about their unique ID when requested,
                    //  so this check could prevent NullPointerException in this case
                    if (name != null) {
                        nicknames.add(name);
                    }
                }
            }
        }, plugin);

        // Save new joining player names so our suggestions are more wide
        // TODO: Unregister this listener on Platform#unregister()
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
        return ParseResult.async(() -> {
            OfflinePlayer offlinePlayer = server.getOfflinePlayer(argument);
            if (!offlinePlayer.hasPlayedBefore() && !allowParseUnknownPlayers) {
                return ParseResult.failure(messageRegistry.get(LiteBukkitMessages.OFFLINE_PLAYER_NOT_FOUND, invocation, argument));
            }
            return ParseResult.success(offlinePlayer);
        });
    }

    @Override
    protected boolean match(Invocation<CommandSender> invocation, Argument<OfflinePlayer> context, String argument) {
        return playerNamePattern.matcher(argument).matches();
    }

    @Override
    public SuggestionResult suggest(Invocation<CommandSender> invocation, Argument<OfflinePlayer> argument, SuggestionContext context) {
        if (nicknames.size() < SUGGESTION_LIMIT) {
            return SuggestionResult.of(nicknames);
        }

        String input = context.getCurrent().multilevel();

        if (input.isEmpty()) {
            return nicknames.stream()
                .limit(SUGGESTION_LIMIT)
                .collect(SuggestionResult.collector());
        }

        return nicknames.subSet(input, input + Character.MAX_VALUE).stream()
            .limit(SUGGESTION_LIMIT)
            .collect(SuggestionResult.collector());
    }

}
