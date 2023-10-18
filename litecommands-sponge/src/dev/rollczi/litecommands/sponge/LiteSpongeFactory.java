package dev.rollczi.litecommands.sponge;

import dev.rollczi.litecommands.LiteCommandsBuilder;
import dev.rollczi.litecommands.LiteCommandsFactory;
import dev.rollczi.litecommands.sponge.internal.LiteSpongePlatform;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.plugin.PluginContainer;

public class LiteSpongeFactory {

    private LiteSpongeFactory() {
    }

    public static LiteCommandsBuilder<CommandCause, LiteSpongeSettings, ?> builder(PluginContainer plugin) {
        return builder(plugin, new LiteSpongeSettings());
    }

    public static LiteCommandsBuilder<CommandCause, LiteSpongeSettings, ?> builder(PluginContainer plugin, LiteSpongeSettings liteBungeeSettings) {
        return LiteCommandsFactory.builder(CommandCause.class, new LiteSpongePlatform(plugin, liteBungeeSettings));
        // TODO
    }
}
