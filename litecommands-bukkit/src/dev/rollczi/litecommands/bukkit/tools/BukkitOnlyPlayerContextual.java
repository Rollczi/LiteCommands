package dev.rollczi.litecommands.bukkit.tools;

import dev.rollczi.litecommands.context.LegacyContextProvider;
import dev.rollczi.litecommands.invocation.Invocation;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import panda.std.Option;
import panda.std.Result;

public class BukkitOnlyPlayerContextual<MESSAGE> implements LegacyContextProvider<CommandSender, Player> {

    private final MESSAGE onlyPlayerMessage;

    public BukkitOnlyPlayerContextual(MESSAGE onlyPlayerMessage) {
        this.onlyPlayerMessage = onlyPlayerMessage;
    }

    @Override
    public Result<Player, Object> provideLegacy(Invocation<CommandSender> invocation) {
        return Option.of(invocation.sender()).is(Player.class).toResult(onlyPlayerMessage);
    }

}
