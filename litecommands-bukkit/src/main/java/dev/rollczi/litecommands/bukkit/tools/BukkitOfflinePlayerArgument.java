package dev.rollczi.litecommands.bukkit.tools;

import dev.rollczi.litecommands.argument.ArgumentName;
import dev.rollczi.litecommands.argument.simple.OneArgument;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.suggestion.Suggestion;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.entity.HumanEntity;
import panda.std.Option;
import panda.std.Result;

import java.util.List;
import java.util.stream.Collectors;

@ArgumentName("offline-player")
public class BukkitOfflinePlayerArgument<MESSAGE> implements OneArgument<OfflinePlayer> {

    private final Server server;
    private final MESSAGE playerNotFoundMessage;

    public BukkitOfflinePlayerArgument(Server server, MESSAGE playerNotFoundMessage) {
        this.server = server;
        this.playerNotFoundMessage = playerNotFoundMessage;
    }

    @Override
    public Result<OfflinePlayer, Object> parse(LiteInvocation invocation, String argument) {
        return Option.of(this.server.getOfflinePlayer(argument)).toResult(playerNotFoundMessage);
    }

    @Override
    public List<Suggestion> suggest(LiteInvocation invocation) {
        return server.getOnlinePlayers().stream()
                .map(HumanEntity::getName)
                .map(Suggestion::of)
                .collect(Collectors.toList());
    }

}
