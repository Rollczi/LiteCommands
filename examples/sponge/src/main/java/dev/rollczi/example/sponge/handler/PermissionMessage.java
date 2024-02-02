package dev.rollczi.example.sponge.handler;

import dev.rollczi.litecommands.handler.result.ResultHandlerChain;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.permission.MissingPermissions;
import dev.rollczi.litecommands.permission.MissingPermissionsHandler;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.spongepowered.api.command.CommandCause;

public class PermissionMessage implements MissingPermissionsHandler<CommandCause> {

    @Override
    public void handle(Invocation<CommandCause> invocation, MissingPermissions missingPermissions, ResultHandlerChain<CommandCause> chain) {
        invocation.sender().audience().sendMessage(MiniMessage.miniMessage().deserialize(
            "<red>You don't have permission to use this command! (<permission>)",
            Placeholder.unparsed("permission", missingPermissions.asJoinedText())
        ));
    }

}
