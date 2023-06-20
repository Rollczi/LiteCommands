package dev.rollczi.litecommands.minestom.tools;

import dev.rollczi.litecommands.context.LegacyContextProvider;
import dev.rollczi.litecommands.invocation.Invocation;
import net.minestom.server.command.CommandSender;
import net.minestom.server.entity.Player;
import panda.std.Option;
import panda.std.Result;

public class MinestomOnlyPlayerContext<MESSAGE> implements LegacyContextProvider<CommandSender, Player> {

    private final MESSAGE onlyPlayerMessage;

    public MinestomOnlyPlayerContext(MESSAGE onlyPlayerMessage) {
        this.onlyPlayerMessage = onlyPlayerMessage;
    }

    @Override
    public Result<Player, Object> provideLegacy(Invocation<CommandSender> invocation) {
        return Option.of(invocation.sender()).is(Player.class).toResult(onlyPlayerMessage);
    }

}
