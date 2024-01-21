package dev.rollczi.example.minestom.handler;

import dev.rollczi.litecommands.handler.result.ResultHandlerChain;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.permission.MissingPermissions;
import dev.rollczi.litecommands.permission.MissingPermissionsHandler;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.minestom.server.command.CommandSender;

public class MyPermissionMessage implements MissingPermissionsHandler<CommandSender> {

    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    @Override
    public void handle(Invocation<CommandSender> invocation, MissingPermissions missingPermissions, ResultHandlerChain<CommandSender> chain) {
        CommandSender commandSource = invocation.sender();
        TagResolver.Single permission = Placeholder.parsed("permission", missingPermissions.asJoinedText());

        commandSource.sendMessage(MINI_MESSAGE.deserialize("<red>You don't have permission to use this command! (<permission>)", permission));
    }

}
