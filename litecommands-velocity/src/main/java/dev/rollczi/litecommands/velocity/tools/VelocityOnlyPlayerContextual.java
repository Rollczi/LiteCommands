package dev.rollczi.litecommands.velocity.tools;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import dev.rollczi.litecommands.command.Invocation;
import dev.rollczi.litecommands.contextual.Contextual;
import panda.std.Option;
import panda.std.Result;

public class VelocityOnlyPlayerContextual<MESSAGE> implements Contextual<CommandSource, Player> {

    private final MESSAGE onlyPlayerMessage;

    public VelocityOnlyPlayerContextual(MESSAGE onlyPlayerMessage) {
        this.onlyPlayerMessage = onlyPlayerMessage;
    }

    @Override
    public Result<Player, Object> extract(CommandSource source, Invocation<CommandSource> invocation) {
        return Option.of(source).is(Player.class).toResult(onlyPlayerMessage);
    }

}
