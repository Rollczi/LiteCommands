package dev.rollczi.litecommands.fabric;

import dev.rollczi.litecommands.LiteCommandsBaseBuilder;
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
import dev.rollczi.litecommands.permission.PermissionResolver;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

import static dev.rollczi.litecommands.reflect.type.TypeRange.upwards;

public final class LiteFabricFactory {

    private LiteFabricFactory() {
    }

    /**
     * @deprecated Use {@link LiteFabricFactory#builder()} instead
     */
    @Deprecated
    public static <B extends LiteCommandsBaseBuilder<CommandSourceStack, LiteFabricSettings, B>> B create() {
        return builder();
    }

    /**
     * @deprecated Use {@link LiteFabricFactory#server()} instead
     */
    @Deprecated
    public static <B extends LiteCommandsBaseBuilder<CommandSourceStack, LiteFabricSettings, B>> B builder() {
        return server();
    }

    @SuppressWarnings("unchecked")
    public static <B extends LiteCommandsBaseBuilder<CommandSourceStack, LiteFabricSettings, B>> B server() {
        return LiteCommandsFactory.<CommandSourceStack, LiteFabricSettings, B>builder(CommandSourceStack.class, builder -> new FabricServerPlatform(new LiteFabricSettings(), builder.getPermissionService()))
            .self((builder, internal) -> {
                MessageRegistry<CommandSourceStack> messages = internal.getMessageRegistry();

                builder
                    .advanced()
                    .permissionResolver(PermissionResolver.createDefault((sender, permission) -> true))
                    .context(ServerPlayer.class, new FabricOnlyPlayerContext<>(source -> source.getPlayer(), messages))
                    .scheduler(new FabricServerScheduler())

                    .argument(upwards(Player.class), new PlayerArgument<>(messages))
                    .argument(upwards(Level.class), new WorldArgument<>(messages))

                    .result(String.class, (invocation, text, chain) -> invocation.sender().sendSuccess(() -> Component.literal(text), false))
                    .result(Component.class, (invocation, text, chain) -> invocation.sender().sendSuccess(() -> text, false))
                ;
            });
    }

    @Environment(EnvType.CLIENT)
    public static <B extends LiteCommandsBaseBuilder<FabricClientCommandSource, LiteFabricSettings, B>> B client() {
        return LiteCommandsFactory.<FabricClientCommandSource, LiteFabricSettings, B>builder(FabricClientCommandSource.class, builder -> new FabricClientPlatform(new LiteFabricSettings(), builder.getPermissionService()))
            .self((builder, internal) -> {
                MessageRegistry<FabricClientCommandSource> messages = internal.getMessageRegistry();

                builder
                    .advanced()
                    .permissionResolver(PermissionResolver.createDefault((sender, permission) -> true))
                    .context(LocalPlayer.class, new FabricOnlyPlayerContext<>(source -> source.getPlayer(), messages))

                    .argument(upwards(Player.class), new ClientPlayerArgument<>(messages))
                    .scheduler(new FabricClientScheduler())

                    .result(String.class, (invocation, text, chain) -> invocation.sender().sendFeedback(Component.literal(text)))
                    .result(Component.class, (invocation, text, chain) -> invocation.sender().sendFeedback(text))
                ;
            });
    }

}
