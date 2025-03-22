package dev.rollczi.litecommands.sponge;

import dev.rollczi.litecommands.LiteCommandsBaseBuilder;
import dev.rollczi.litecommands.LiteCommandsBuilder;
import dev.rollczi.litecommands.LiteCommandsFactory;
import dev.rollczi.litecommands.adventure.LiteAdventureExtension;
import dev.rollczi.litecommands.message.MessageRegistry;
import dev.rollczi.litecommands.permission.PermissionResolver;
import dev.rollczi.litecommands.sponge.argument.ServerPlayerArgument;
import dev.rollczi.litecommands.sponge.context.ServerPlayerOnlyContext;
import org.spongepowered.api.Client;
import org.spongepowered.api.Game;
import org.spongepowered.api.Server;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.plugin.PluginContainer;

public class LiteSpongeFactory {

    private LiteSpongeFactory() {
    }

    public static <B extends LiteCommandsBuilder<CommandCause, LiteSpongeSettings, B>> B builder(PluginContainer plugin, Game game) {
        return builder(plugin, game, new LiteSpongeSettings());
    }

    @SuppressWarnings("unchecked")
    public static <B extends LiteCommandsBuilder<CommandCause, LiteSpongeSettings, B>> B builder(PluginContainer plugin, Game game, LiteSpongeSettings settings) {
        return (B) builder0(plugin, game, settings);
    }

    private static <B extends LiteCommandsBaseBuilder<CommandCause, LiteSpongeSettings, B>> B builder0(PluginContainer plugin, Game game, LiteSpongeSettings settings) {
        return LiteCommandsFactory.<CommandCause, LiteSpongeSettings, B>builder(CommandCause.class, builder -> new SpongePlatform(plugin, settings, builder.getPermissionService())).self((builder, internal) -> {
            MessageRegistry<CommandCause> messageRegistry = internal.getMessageRegistry();

            builder
                .permissionResolver(PermissionResolver.createDefault(CommandCause.class, (sender, permission) -> sender.hasPermission(permission)))

                .bind(Server.class, () -> game.server())
                .bind(Client.class, () -> game.client())
                .bind(Game.class, () -> game)
                .bind(PluginContainer.class, () -> plugin)

                .argument(ServerPlayer.class, new ServerPlayerArgument(game, messageRegistry))
                .context(ServerPlayer.class, new ServerPlayerOnlyContext(messageRegistry))
                .scheduler(new SpongeScheduler(plugin, game))

                .extension(new LiteAdventureExtension<>(invocation -> invocation.sender().audience()));
        });
    }
}
