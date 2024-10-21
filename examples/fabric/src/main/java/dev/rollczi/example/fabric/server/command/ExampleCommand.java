package dev.rollczi.example.fabric.server.command;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.join.Join;
import dev.rollczi.litecommands.annotations.quoted.Quoted;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

@Command(name = "example")
public class ExampleCommand {
    @Execute(name = "kick")
    void sendMessage(@Arg("player") ServerPlayerEntity player, @Join("reason") String reason) {
        player.networkHandler.disconnect(Text.of(reason));
    }

    @Execute(name = "message")
    Text sendMessage(@Quoted @Arg String message) {
        return Text.of("You saied: " + message);
    }
}
