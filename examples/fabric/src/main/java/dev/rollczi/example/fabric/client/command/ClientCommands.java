package dev.rollczi.example.fabric.client.command;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.async.Async;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Sender;
import dev.rollczi.litecommands.annotations.execute.Execute;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;

@Command(name = "litecommands", aliases = "litecmd")
public class ClientCommands {

    @Execute
    Text info() {
        return Text.of("Hello from LiteCommands!");
    }

    @Execute(name = "my", aliases = "myName")
    Text myName(@Sender FabricClientCommandSource sender) {
        return sender.getPlayer().getName();
    }

    @Execute(name = "sleeping")
    String ping(@Sender ClientPlayerEntity player) {
        return "You are " + (player.isSleeping() ? "" : "not ") + "sleeping";
    }

    @Execute(name = "time")
    String currentTime() {
        return String.valueOf(System.currentTimeMillis());
    }

    @Execute(name = "health")
    String health(@Arg ClientPlayerEntity player) {
        return String.valueOf(player.getHealth());
    }

    @Execute(name = "thread1")
    String thread1() {
        return Thread.currentThread().getName();
    }

    @Execute(name = "thread2")
    @Async
    String thread2() {
        return Thread.currentThread().getName();
    }
}
