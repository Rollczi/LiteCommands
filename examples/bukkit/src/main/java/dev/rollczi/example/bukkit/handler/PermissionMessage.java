package dev.rollczi.example.bukkit.handler;

import dev.rollczi.example.bukkit.util.ChatUtil;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.command.permission.RequiredPermissions;
import dev.rollczi.litecommands.handle.PermissionHandler;
import org.bukkit.command.CommandSender;

public class PermissionMessage implements PermissionHandler<CommandSender> {

    @Override
    public void handle(CommandSender sender, LiteInvocation invocation, RequiredPermissions requiredPermissions) {
        sender.sendMessage(ChatUtil.color("&cNie masz permisji do tej komendy! &7(" + String.join(", ", requiredPermissions.getPermissions()) + ")"));
    }

}
