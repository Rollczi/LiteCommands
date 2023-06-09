package dev.rollczi.litecommands.bukkit.tools;

import dev.rollczi.litecommands.context.ContextProvider;
import dev.rollczi.litecommands.invocation.Invocation;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import panda.std.Option;
import panda.std.Result;

public class BukkitOnlyPlayerContextual<MESSAGE> implements ContextProvider<CommandSender, Player> {

    private final MESSAGE onlyPlayerMessage;

    public BukkitOnlyPlayerContextual(MESSAGE onlyPlayerMessage) {
        this.onlyPlayerMessage = onlyPlayerMessage;
    }

    @Override
    public Result<Player, Object> provide(Invocation<CommandSender> invocation) {
        return Option.of(invocation.sender()).is(Player.class).toResult(onlyPlayerMessage);
    }

}
