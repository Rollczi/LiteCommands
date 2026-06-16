package dev.rollczi.example.fabric.server.command;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.async.Async;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.join.Join;
import dev.rollczi.litecommands.annotations.permission.Permission;
import dev.rollczi.litecommands.annotations.quoted.Quoted;
import dev.rollczi.litecommands.platform.PlatformSender;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

@Command(name = "example", aliases = "tutorial")
public class ExampleCommand {
    @Execute(name = "kick")
    void sendMessage(@Arg("player") ServerPlayer player, @Join("reason") String reason) {
        player.connection.disconnect(Component.literal(reason));
    }

    @Execute(name = "message", aliases = "msg")
    Component sendMessage(@Quoted @Arg String message) {
        return Component.literal("You saied: " + message);
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

    @Execute(name = "admin")
    @Permission("permission.admin")
    String admin(@Context PlatformSender sender) {
        return "Hi " + sender.getName() + ", you have permission!";
    }

}
