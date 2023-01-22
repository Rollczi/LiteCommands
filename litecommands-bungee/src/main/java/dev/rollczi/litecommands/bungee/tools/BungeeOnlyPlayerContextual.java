package dev.rollczi.litecommands.bungee.tools;

import dev.rollczi.litecommands.command.Invocation;
import dev.rollczi.litecommands.contextual.Contextual;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import panda.std.Option;
import panda.std.Result;

public class BungeeOnlyPlayerContextual<MESSAGE> implements Contextual<CommandSender, ProxiedPlayer> {

    private final MESSAGE onlyPlayerMessage;

    public BungeeOnlyPlayerContextual(MESSAGE onlyPlayerMessage) {
        this.onlyPlayerMessage = onlyPlayerMessage;
    }

    @Override
    public Result<ProxiedPlayer, Object> extract(CommandSender sender, Invocation<CommandSender> invocation) {
        return Option.of(sender).is(ProxiedPlayer.class).toResult(onlyPlayerMessage);
    }

}
