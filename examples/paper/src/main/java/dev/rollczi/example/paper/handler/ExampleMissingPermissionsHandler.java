package dev.rollczi.example.paper.handler;

import dev.rollczi.example.paper.util.ChatUtil;
import dev.rollczi.litecommands.handler.result.ResultHandlerChain;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.permission.MissingPermissions;
import dev.rollczi.litecommands.permission.MissingPermissionsHandler;
import org.bukkit.command.CommandSender;

public class ExampleMissingPermissionsHandler implements MissingPermissionsHandler<CommandSender> {

    @Override
    public void handle(Invocation<CommandSender> invocation, MissingPermissions missingPermissions, ResultHandlerChain<CommandSender> chain) {
        String permissions = missingPermissions.asJoinedText();
        CommandSender sender = invocation.sender();

        sender.sendMessage(ChatUtil.color("You don't have permission to use this command! &7(" + permissions + ")"));
    }

}
