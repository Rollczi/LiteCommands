package dev.rollczi.litecommands.fabric;

import dev.rollczi.litecommands.LiteCommandsBuilder;
import dev.rollczi.litecommands.LiteCommandsFactory;
import dev.rollczi.litecommands.fabric.argument.PlayerArgument;
import dev.rollczi.litecommands.fabric.argument.WorldArgument;
import dev.rollczi.litecommands.fabric.tools.FabricOnlyPlayerContextual;
import dev.rollczi.litecommands.message.MessageRegistry;
import dev.rollczi.litecommands.platform.PlatformSettings;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.world.World;

/**
 * 2024/2/13<br>
 * LiteCommands<br>
 *
 * @author huanmeng_qwq
 */
public class LiteFabricFactory {
    @SuppressWarnings("unused")
    public static final LiteFabricFactory INSTANCE = new LiteFabricFactory();

    private LiteFabricFactory() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            for (FabricCommand instance : FabricCommand.INSTANCES) {
                instance.register(dispatcher);
            }
        });
    }

    @SuppressWarnings("unchecked")
    public static <B extends LiteCommandsBuilder<ServerCommandSource, PlatformSettings, B>> B create() {
        return (B) LiteCommandsFactory.builder(ServerCommandSource.class, new FabricPlatform(new LiteFabricSettings()))
            .selfProcessor((builder, internal) -> {
                MessageRegistry<ServerCommandSource> messageRegistry = internal.getMessageRegistry();

                builder
                    .context(ServerPlayerEntity.class, new FabricOnlyPlayerContextual<>(Text.of("Only players can use this command! (Set this message in LiteFabricFactory")))
                    .context(PlayerEntity.class, new FabricOnlyPlayerContextual<>(Text.of("Only players can use this command! (Set this message in LiteFabricFactory")))
                    .result(String.class, new StringHandler())
                    .result(Text.class, new TextHandler())

                    .argument(PlayerEntity.class, new PlayerArgument<>(messageRegistry))
                    .argument(ServerPlayerEntity.class, new PlayerArgument<>(messageRegistry))

                    .argument(World.class, new WorldArgument<>(messageRegistry))
                    .argument(ServerWorld.class, new WorldArgument<>(messageRegistry))
                ;
            })
            ;
    }
}
