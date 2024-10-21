package dev.rollczi.litecommands.fabric;

import dev.rollczi.litecommands.LiteCommandsBuilder;
import dev.rollczi.litecommands.LiteCommandsFactory;
import dev.rollczi.litecommands.fabric.client.argument.ClientPlayerArgument;
import dev.rollczi.litecommands.fabric.common.StringHandler;
import dev.rollczi.litecommands.fabric.common.TextHandler;
import dev.rollczi.litecommands.fabric.server.argument.PlayerArgument;
import dev.rollczi.litecommands.fabric.server.argument.WorldArgument;
import dev.rollczi.litecommands.fabric.common.context.FabricOnlyPlayerContext;
import dev.rollczi.litecommands.message.MessageRegistry;
import dev.rollczi.litecommands.platform.PlatformSettings;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.world.World;

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

    public static <B extends LiteCommandsBuilder<ServerCommandSource, PlatformSettings, B>> B builder() {
        return server();
    }

    @SuppressWarnings("unchecked")
    public static <B extends LiteCommandsBuilder<ServerCommandSource, PlatformSettings, B>> B server() {
        return (B) LiteCommandsFactory.builder(ServerCommandSource.class, new FabricServerPlatform(new LiteFabricSettings()))
            .selfProcessor((builder, internal) -> {
                MessageRegistry<ServerCommandSource> messageRegistry = internal.getMessageRegistry();

                builder
                    .context(ServerPlayerEntity.class, new FabricOnlyPlayerContext<>(ServerCommandSource::getPlayer, messageRegistry))
                    .context(PlayerEntity.class, new FabricOnlyPlayerContext<>(ServerCommandSource::getPlayer, messageRegistry))
                    .result(String.class, new StringHandler<>((source, str) -> source.sendFeedback(() -> Text.of(str), false)))
                    .result(Text.class, new TextHandler<>((source, text) -> source.sendFeedback(() -> text, false)))
                    .argument(PlayerEntity.class, new PlayerArgument<>(messageRegistry))
                    .argument(ServerPlayerEntity.class, new PlayerArgument<>(messageRegistry))

                    .argument(World.class, new WorldArgument<>(messageRegistry))
                    .argument(ServerWorld.class, new WorldArgument<>(messageRegistry))
                ;
            });
    }

    @SuppressWarnings("unchecked")
    @Environment(EnvType.CLIENT)
    public static <B extends LiteCommandsBuilder<FabricClientCommandSource, PlatformSettings, B>> B client() {
        return (B) LiteCommandsFactory.builder(FabricClientCommandSource.class, new FabricClientPlatform(new LiteFabricSettings()))
            .selfProcessor((builder, internal) -> {
                MessageRegistry<FabricClientCommandSource> messageRegistry = internal.getMessageRegistry();

                builder
                    .context(AbstractClientPlayerEntity.class, new FabricOnlyPlayerContext<>(FabricClientCommandSource::getPlayer, messageRegistry))
                    .context(ClientPlayerEntity.class, new FabricOnlyPlayerContext<>(FabricClientCommandSource::getPlayer, messageRegistry))
                    .context(PlayerEntity.class, new FabricOnlyPlayerContext<>(FabricClientCommandSource::getPlayer, messageRegistry))
                    .result(String.class, new StringHandler<>((source, str) -> source.sendFeedback(Text.of(str))))
                    .result(Text.class, new TextHandler<>(FabricClientCommandSource::sendFeedback))
                    .argument(PlayerEntity.class, new ClientPlayerArgument<>(messageRegistry))
                    .argument(AbstractClientPlayerEntity.class, new ClientPlayerArgument<>(messageRegistry))
                ;
            });
    }

}
