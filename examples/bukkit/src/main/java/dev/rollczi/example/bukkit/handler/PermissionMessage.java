package dev.rollczi.example.bukkit.handler;

import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.command.permission.LitePermissions;
import dev.rollczi.litecommands.handle.PermissionHandler;
import org.bukkit.command.CommandSender;
import panda.utilities.text.Joiner;

public class PermissionMessage implements PermissionHandler<CommandSender> {

    @Override
    public void handle(CommandSender sender, LiteInvocation invocation, LitePermissions litePermissions) {
        sender.sendMessage("&cNie masz permisji do tej komendy! &7(" + Joiner.on(", ").join(litePermissions.getPermissions()) + ")");
    }

}
