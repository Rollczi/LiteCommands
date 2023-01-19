package dev.rollczi.litecommands.minestom.tools;

import dev.rollczi.litecommands.command.Invocation;
import dev.rollczi.litecommands.contextual.Contextual;
import net.minestom.server.command.CommandSender;
import net.minestom.server.entity.Player;
import panda.std.Option;
import panda.std.Result;

public class MinestomOnlyPlayerContextual<MESSAGE> implements Contextual<CommandSender, Player> {

    private final MESSAGE onlyPlayerMessage;

    public MinestomOnlyPlayerContextual(MESSAGE onlyPlayerMessage) {
        this.onlyPlayerMessage = onlyPlayerMessage;
    }

    @Override
    public Result<Player, Object> extract(CommandSender sender, Invocation<CommandSender> invocation) {
        return Option.of(sender).is(Player.class).toResult(onlyPlayerMessage);
    }

}
