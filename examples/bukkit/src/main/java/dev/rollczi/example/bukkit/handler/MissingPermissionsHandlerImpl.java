package dev.rollczi.example.bukkit.handler;

import dev.rollczi.example.bukkit.util.ChatUtil;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.permission.MissingPermissions;
import dev.rollczi.litecommands.permission.MissingPermissionsHandler;
import org.bukkit.command.CommandSender;

public class MissingPermissionsHandlerImpl implements MissingPermissionsHandler<CommandSender> {

    @Override
    public void handle(Invocation<CommandSender> invocation, MissingPermissions missingPermissions) {
        invocation.getSender().sendMessage(ChatUtil.color("You don't have permission to use this command! &7(" + missingPermissions.asJoinedText() + ")"));
    }

}
