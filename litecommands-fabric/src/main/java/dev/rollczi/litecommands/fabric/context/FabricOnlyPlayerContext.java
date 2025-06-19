package dev.rollczi.litecommands.fabric.context;

import dev.rollczi.litecommands.context.ContextProvider;
import dev.rollczi.litecommands.context.ContextResult;
import dev.rollczi.litecommands.fabric.LiteFabricMessages;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.message.MessageRegistry;
import net.minecraft.entity.player.PlayerEntity;

import java.util.function.Function;
import org.jetbrains.annotations.Nullable;

public class FabricOnlyPlayerContext<SOURCE, P extends PlayerEntity> implements ContextProvider<SOURCE, P> {

    private final Function<SOURCE, @Nullable P> playerProvider;
    private final MessageRegistry<SOURCE> messageRegistry;

    public FabricOnlyPlayerContext(Function<SOURCE, @Nullable P> playerProvider, MessageRegistry<SOURCE> messageRegistry) {
        this.playerProvider = playerProvider;
        this.messageRegistry = messageRegistry;
    }

    @Override
    public ContextResult<P> provide(Invocation<SOURCE> invocation) {
        P player = this.playerProvider.apply(invocation.sender());

        if (player != null) {
            return ContextResult.ok(() -> player);
        }

        return ContextResult.error(messageRegistry.get(LiteFabricMessages.PLAYER_ONLY, invocation));
    }

}
