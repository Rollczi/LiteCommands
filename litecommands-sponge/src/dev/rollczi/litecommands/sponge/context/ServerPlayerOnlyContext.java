package dev.rollczi.litecommands.sponge.context;

import dev.rollczi.litecommands.context.ContextProvider;
import dev.rollczi.litecommands.context.ContextResult;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.message.MessageRegistry;
import dev.rollczi.litecommands.sponge.LiteSpongeMessages;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

public class ServerPlayerOnlyContext implements ContextProvider<CommandCause, ServerPlayer> {

    private final MessageRegistry<CommandCause> messageRegistry;

    public ServerPlayerOnlyContext(MessageRegistry<CommandCause> messageRegistry) {
        this.messageRegistry = messageRegistry;
    }

    @Override
    public ContextResult<ServerPlayer> provide(Invocation<CommandCause> invocation) {
        if (invocation.sender().root() instanceof ServerPlayer) {
            return ContextResult.ok(() -> (ServerPlayer) invocation.sender().root());
        }

        return ContextResult.error(messageRegistry.get(LiteSpongeMessages.PLAYER_ONLY, invocation));
    }

}
