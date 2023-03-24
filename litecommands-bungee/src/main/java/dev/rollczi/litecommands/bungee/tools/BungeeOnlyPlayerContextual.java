package dev.rollczi.litecommands.bungee.tools;

import dev.rollczi.litecommands.bind.BindContextual;
import dev.rollczi.litecommands.invocation.Invocation;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import panda.std.Option;
import panda.std.Result;

import java.util.function.Supplier;

public class BungeeOnlyPlayerContextual<MESSAGE> implements BindContextual<CommandSender, ProxiedPlayer> {

    private final Supplier<MESSAGE> onlyPlayerMessage;

    public BungeeOnlyPlayerContextual(Supplier<MESSAGE> onlyPlayerMessage) {
        this.onlyPlayerMessage = onlyPlayerMessage;
    }

    public BungeeOnlyPlayerContextual(MESSAGE onlyPlayerMessage) {
        this(() -> onlyPlayerMessage);
    }

    @Override
    public Result<ProxiedPlayer, Object> extract(Invocation<CommandSender> invocation) {
        return Option.of(invocation.getSender())
            .is(ProxiedPlayer.class)
            .toResult(onlyPlayerMessage.get());
    }

}
