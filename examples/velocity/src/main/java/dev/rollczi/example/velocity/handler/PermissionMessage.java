package dev.rollczi.example.velocity.handler;

import com.velocitypowered.api.command.CommandSource;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.command.permission.RequiredPermissions;
import dev.rollczi.litecommands.handle.PermissionHandler;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

public class PermissionMessage implements PermissionHandler<CommandSource> {

    private final static MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    @Override
    public void handle(CommandSource commandSource, LiteInvocation invocation, RequiredPermissions requiredPermissions) {
        TagResolver.Single permission = Placeholder.parsed("permission", String.join(", ", requiredPermissions.getPermissions()));

        commandSource.sendMessage(MINI_MESSAGE.deserialize("<red>You don't have permission to use this command! (<permission>)", permission));
    }

}
