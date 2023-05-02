package dev.rollczi.example.velocity.handler;

import com.velocitypowered.api.command.CommandSource;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.permission.MissingPermissions;
import dev.rollczi.litecommands.permission.MissingPermissionsHandler;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

public class PermissionMessage implements MissingPermissionsHandler<CommandSource> {

    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    @Override
    public void handle(Invocation<CommandSource> invocation, MissingPermissions missingPermissions) {
        CommandSource commandSource = invocation.sender();
        TagResolver.Single permission = Placeholder.parsed("permission", missingPermissions.asJoinedText());

        commandSource.sendMessage(MINI_MESSAGE.deserialize("<red>You don't have permission to use this command! (<permission>)", permission));
    }

}
