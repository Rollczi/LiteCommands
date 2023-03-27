package dev.rollczi.example.velocity.command;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import dev.rollczi.litecommands.argument.Arg;
import dev.rollczi.litecommands.command.execute.Execute;
import dev.rollczi.litecommands.command.route.Route;

@Route(name = "send")
public class SendCommand {

    @Execute
    public void send(@Arg Player player, @Arg RegisteredServer server) {
        player.createConnectionRequest(server).connect().whenComplete((result, throwable) -> {

        });
    }

}
