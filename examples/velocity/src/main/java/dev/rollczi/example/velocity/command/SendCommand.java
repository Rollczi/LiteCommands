package dev.rollczi.example.velocity.command;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import dev.rollczi.litecommands.annotations.argument.arg.Arg;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.command.Command;
import net.kyori.adventure.text.minimessage.MiniMessage;

@Command(name = "send")
public class SendCommand {

    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    @Execute
    public void send(@Arg Player player, @Arg RegisteredServer server) {
        player.createConnectionRequest(server).connect().whenComplete((result, throwable) -> {
            player.sendMessage(miniMessage.deserialize("<green> You have been sent to <gray>" + server.getServerInfo().getName() + "<green>!"));
        });
    }

}
