package dev.rollczi.example.minestom.command;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.join.Join;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.entity.Player;

@Command(name = "message", aliases = {"msg"})
public class MsgCommand {

    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    @Execute
    void execute(@Context Player player, @Arg("player") Player target, @Join("message") String message) {
        player.sendMessage(miniMessage.deserialize("<gray>[<yellow>Me<gray> -> <yellow>" + target.getUsername() + "<gray>] <white>" + message));
        target.sendMessage(miniMessage.deserialize("<gray>[<yellow>" + player.getUsername() + "<gray> -> <yellow>Me<gray>] <white>" + message));
    }

}
