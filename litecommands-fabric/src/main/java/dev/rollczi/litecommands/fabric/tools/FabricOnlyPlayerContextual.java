package dev.rollczi.litecommands.fabric.tools;

import dev.rollczi.litecommands.context.ContextProvider;
import dev.rollczi.litecommands.context.ContextResult;
import dev.rollczi.litecommands.invocation.Invocation;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.function.Supplier;

/**
 * 2024/2/13<br>
 * LiteCommands<br>
 *
 * @author huanmeng_qwq
 */
public class FabricOnlyPlayerContextual<P extends PlayerEntity> implements ContextProvider<ServerCommandSource, P> {
    private final Supplier<Text> onlyPlayerMessage;

    public FabricOnlyPlayerContextual(Supplier<Text> onlyPlayerMessage) {
        this.onlyPlayerMessage = onlyPlayerMessage;
    }

    public FabricOnlyPlayerContextual(Text onlyPlayerMessage) {
        this(() -> onlyPlayerMessage);
    }

    @SuppressWarnings("unchecked")
    @Override
    public ContextResult<P> provide(Invocation<ServerCommandSource> invocation) {
        ServerPlayerEntity player = invocation.sender().getPlayer();
        if (player != null) {
            return ContextResult.ok(() -> (P) player);
        }

        return ContextResult.error(onlyPlayerMessage.get());
    }
}
