package dev.rollczi.litecommands.fabric.context;

import dev.rollczi.litecommands.context.ContextProvider;
import dev.rollczi.litecommands.context.ContextResult;
import dev.rollczi.litecommands.fabric.LiteFabricMessages;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.message.MessageRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

public class FabricOnlyPlayerContext<P extends PlayerEntity> implements ContextProvider<ServerCommandSource, P> {

    private final MessageRegistry<ServerCommandSource> messageRegistry;

    public FabricOnlyPlayerContext(MessageRegistry<ServerCommandSource> messageRegistry) {
        this.messageRegistry = messageRegistry;
    }

    @SuppressWarnings("unchecked")
    @Override
    public ContextResult<P> provide(Invocation<ServerCommandSource> invocation) {
        ServerPlayerEntity player = invocation.sender().getPlayer();
        if (player != null) {
            return ContextResult.ok(() -> (P) player);
        }

        return ContextResult.error(messageRegistry.getInvoked(LiteFabricMessages.PLAYER_ONLY, invocation));
    }

}
