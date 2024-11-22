package dev.rollczi.litecommands.fabric;

import dev.rollczi.litecommands.LiteCommandsBuilder;
import dev.rollczi.litecommands.LiteCommandsFactory;
import dev.rollczi.litecommands.fabric.client.FabricClientPlatform;
import dev.rollczi.litecommands.fabric.client.FabricClientScheduler;
import dev.rollczi.litecommands.fabric.client.argument.ClientPlayerArgument;
import dev.rollczi.litecommands.fabric.context.FabricOnlyPlayerContext;
import dev.rollczi.litecommands.fabric.server.FabricServerPlatform;
import dev.rollczi.litecommands.fabric.server.FabricServerScheduler;
import dev.rollczi.litecommands.fabric.server.argument.PlayerArgument;
import dev.rollczi.litecommands.fabric.server.argument.WorldArgument;
import dev.rollczi.litecommands.message.MessageRegistry;
import dev.rollczi.litecommands.platform.PlatformSettings;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.World;

import static dev.rollczi.litecommands.reflect.type.TypeRange.upwards;

public final class LiteFabricFactory {

    private LiteFabricFactory() {
    }

    /**
     * @deprecated Use {@link LiteFabricFactory#builder()} instead
     */
    @Deprecated
    public static <B extends LiteCommandsBuilder<ServerCommandSource, PlatformSettings, B>> B create() {
        return builder();
    }

    /**
     * @deprecated Use {@link LiteFabricFactory#server()} instead
     */
    @Deprecated
    public static <B extends LiteCommandsBuilder<ServerCommandSource, PlatformSettings, B>> B builder() {
        return server();
    }

    @SuppressWarnings("unchecked")
    public static <B extends LiteCommandsBuilder<ServerCommandSource, PlatformSettings, B>> B server() {
        return (B) LiteCommandsFactory.builder(ServerCommandSource.class, new FabricServerPlatform(new LiteFabricSettings()))
            .self((builder, internal) -> {
                MessageRegistry<ServerCommandSource> messages = internal.getMessageRegistry();

                builder
                    .advanced()
                    .context(ServerPlayerEntity.class, new FabricOnlyPlayerContext<>(source -> source.getPlayer(), messages))
                    .scheduler(new FabricServerScheduler())

                    .argument(upwards(PlayerEntity.class), new PlayerArgument<>(messages))
                    .argument(upwards(World.class), new WorldArgument<>(messages))

                    .result(String.class, (invocation, text, chain) -> invocation.sender().sendFeedback(() -> Text.of(text), false))
                    .result(Text.class, (invocation, text, chain) -> invocation.sender().sendFeedback(() -> text, false))
                ;
            });
    }

    @SuppressWarnings("unchecked")
    @Environment(EnvType.CLIENT)
    public static <B extends LiteCommandsBuilder<FabricClientCommandSource, PlatformSettings, B>> B client() {
        return (B) LiteCommandsFactory.builder(FabricClientCommandSource.class, new FabricClientPlatform(new LiteFabricSettings()))
            .self((builder, internal) -> {
                MessageRegistry<FabricClientCommandSource> messages = internal.getMessageRegistry();

                builder
                    .advanced()
                    .context(ClientPlayerEntity.class, new FabricOnlyPlayerContext<>(source -> source.getPlayer(), messages))

                    .argument(upwards(PlayerEntity.class), new ClientPlayerArgument<>(messages))
                    .scheduler(new FabricClientScheduler())

                    .result(String.class, (invocation, text, chain) -> invocation.sender().sendFeedback(Text.of(text)))
                    .result(Text.class, (invocation, text, chain) -> invocation.sender().sendFeedback(text))
                ;
            });
    }

}
