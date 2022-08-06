package dev.rollczi.litecommands.bukkit.tools;

import dev.rollczi.litecommands.command.Invocation;
import dev.rollczi.litecommands.contextual.Contextual;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import panda.std.Option;
import panda.std.Result;

public class BukkitOnlyPlayerContextual<MESSAGE> implements Contextual<CommandSender, Player> {

    private final MESSAGE onlyPlayerMessage;

    public BukkitOnlyPlayerContextual(MESSAGE onlyPlayerMessage) {
        this.onlyPlayerMessage = onlyPlayerMessage;
    }

    @Override
    public Result<Player, Object> extract(CommandSender sender, Invocation<CommandSender> invocation) {
        return Option.of(sender).is(Player.class).toResult(onlyPlayerMessage);
    }

}
