package dev.rollczi.example.velocity.command;

import com.velocitypowered.api.proxy.Player;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.join.Join;
import dev.rollczi.litecommands.annotations.permission.Permission;
import net.kyori.adventure.text.minimessage.MiniMessage;

@Command(name = "message", aliases = {"msg"})
@Permission("example.message")
public class MsgCommand {

    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    @Execute
    public void execute(@Context Player player, @Arg("player") Player target, @Join("message") String message) {
        player.sendMessage(miniMessage.deserialize("<gray>[<yellow>Me<gray> -> <yellow>" + target.getUsername() + "<gray>] <white>" + message));
        target.sendMessage(miniMessage.deserialize("<gray>[<yellow>" + player.getUsername() + "<gray> -> <yellow>Me<gray>] <white>" + message));
    }

}
