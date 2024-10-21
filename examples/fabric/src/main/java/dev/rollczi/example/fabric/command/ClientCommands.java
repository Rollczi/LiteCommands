package dev.rollczi.example.fabric.command;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Sender;
import dev.rollczi.litecommands.annotations.execute.Execute;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

@Command(name = "litecommands")
public class ClientCommands {
    @Execute
    Text info() {
        return Text.of("Hello from LiteCommands!");
    }

    @Execute(name = "my")
    Text myName(@Sender FabricClientCommandSource sender) {
        return sender.getPlayer().getName();
    }

    @Execute(name = "time")
    String currentTime() {
        return String.valueOf(System.currentTimeMillis());
    }

    @Execute(name = "health")
    String health(@Arg PlayerEntity player) {
        return String.valueOf(player.getHealth());
    }
}
