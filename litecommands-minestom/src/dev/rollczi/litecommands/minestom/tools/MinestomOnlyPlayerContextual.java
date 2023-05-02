package dev.rollczi.litecommands.minestom.tools;

import dev.rollczi.litecommands.bind.BindContextual;
import dev.rollczi.litecommands.invocation.Invocation;
import net.minestom.server.command.CommandSender;
import net.minestom.server.entity.Player;
import panda.std.Option;
import panda.std.Result;

public class MinestomOnlyPlayerContextual<MESSAGE> implements BindContextual<CommandSender, Player> {

    private final MESSAGE onlyPlayerMessage;

    public MinestomOnlyPlayerContextual(MESSAGE onlyPlayerMessage) {
        this.onlyPlayerMessage = onlyPlayerMessage;
    }

    @Override
    public Result<Player, Object> extract(Invocation<CommandSender> invocation) {
        return Option.of(invocation.sender()).is(Player.class).toResult(onlyPlayerMessage);
    }

}
