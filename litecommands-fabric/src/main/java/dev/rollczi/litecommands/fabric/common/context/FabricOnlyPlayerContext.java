package dev.rollczi.litecommands.fabric.common.context;

import dev.rollczi.litecommands.context.ContextProvider;
import dev.rollczi.litecommands.context.ContextResult;
import dev.rollczi.litecommands.fabric.LiteFabricMessages;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.message.MessageRegistry;
import net.minecraft.entity.player.PlayerEntity;

import java.util.function.Function;

public class FabricOnlyPlayerContext<SOURCE, P extends PlayerEntity> implements ContextProvider<SOURCE, P> {
    private final Function<SOURCE, P> func;
    private final MessageRegistry<SOURCE> messageRegistry;

    public FabricOnlyPlayerContext(Function<SOURCE, P> func, MessageRegistry<SOURCE> messageRegistry) {
        this.func = func;
        this.messageRegistry = messageRegistry;
    }

    @SuppressWarnings("unchecked")
    @Override
    public ContextResult<P> provide(Invocation<SOURCE> invocation) {
        P player = this.func.apply(invocation.sender());
        if (player != null) {
            return ContextResult.ok(() -> player);
        }

        return ContextResult.error(messageRegistry.getInvoked(LiteFabricMessages.PLAYER_ONLY, invocation));
    }

}
