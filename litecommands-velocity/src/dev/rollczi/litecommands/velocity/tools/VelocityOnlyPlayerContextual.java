package dev.rollczi.litecommands.velocity.tools;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import dev.rollczi.litecommands.bind.BindContextual;
import dev.rollczi.litecommands.invocation.Invocation;
import panda.std.Option;
import panda.std.Result;

public class VelocityOnlyPlayerContextual<MESSAGE> implements BindContextual<CommandSource, Player> {

    private final MESSAGE onlyPlayerMessage;

    public VelocityOnlyPlayerContextual(MESSAGE onlyPlayerMessage) {
        this.onlyPlayerMessage = onlyPlayerMessage;
    }

    @Override
    public Result<Player, Object> extract(Invocation<CommandSource> invocation) {
        return Option.of(invocation.sender()).is(Player.class).toResult(onlyPlayerMessage);
    }

}
