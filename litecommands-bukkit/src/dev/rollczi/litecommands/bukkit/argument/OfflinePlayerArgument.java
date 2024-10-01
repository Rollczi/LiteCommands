package dev.rollczi.litecommands.bukkit.argument;

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import java.util.TreeSet;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import java.time.Instant;

public class OfflinePlayerArgument extends ArgumentResolver<CommandSender, OfflinePlayer> {

    private static final int SUGGESTION_LIMIT = 256;

    private final Server server;
    private final Plugin plugin;
    private final boolean enableThreadCheck;

    private final TreeSet<String> nicknames = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
    private Instant nextWarningDate = Instant.EPOCH;

    public OfflinePlayerArgument(Server server, Plugin plugin, boolean enableThreadCheck) {
        this.server = server;
        this.plugin = plugin;
        this.enableThreadCheck = enableThreadCheck;

        // Server#getOfflinePlayers() can be blocking, so we don't want to call it every time
        for (OfflinePlayer offlinePlayer : server.getOfflinePlayers()) {
            this.nicknames.add(offlinePlayer.getName());
        }

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
        // TODO: Use async argument parsing: https://github.com/Rollczi/LiteCommands/pull/435
        if (enableThreadCheck && server.isPrimaryThread() && Instant.now().isAfter(nextWarningDate)) {
            plugin.getLogger().warning("LiteCommands | OfflinePlayer argument resolved synchronously! This might cause server freeze! Add @Async to '" + context.getName() + "' argument. (command /" + invocation.name() + ")");
            nextWarningDate = Instant.now().plusSeconds(60);
        }

        return ParseResult.success(server.getOfflinePlayer(argument));
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
