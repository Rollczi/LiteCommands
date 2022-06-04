package dev.rollczi.litecommands.bukkit.tools;

import dev.rollczi.litecommands.argument.ArgumentName;
import dev.rollczi.litecommands.argument.simple.OneArgument;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.command.sugesstion.Suggestion;
import org.bukkit.Server;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import panda.std.Option;
import panda.std.Result;

import java.util.List;
import java.util.stream.Collectors;

@ArgumentName("player")
public class BukkitPlayerArgument implements OneArgument<Player> {

    private final Server server;
    private final String playerNotFound;

    public BukkitPlayerArgument(Server server, String playerNotFound) {
        this.server = server;
        this.playerNotFound = playerNotFound;
    }

    @Override
    public Result<Player, Object> parse(LiteInvocation invocation, String argument) {
        return Option.of(this.server.getPlayer(argument)).toResult(playerNotFound);
    }

    @Override
    public List<Suggestion> suggest(LiteInvocation invocation) {
        return server.getOnlinePlayers().stream()
                .map(HumanEntity::getName)
                .map(Suggestion::of)
                .collect(Collectors.toList());
    }

}
