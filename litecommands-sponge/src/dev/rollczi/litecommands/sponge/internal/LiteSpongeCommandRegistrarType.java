package dev.rollczi.litecommands.sponge.internal;

import io.leangen.geantyref.TypeToken;
import org.jetbrains.annotations.ApiStatus;
import org.spongepowered.api.command.manager.CommandManager;
import org.spongepowered.api.command.registrar.CommandRegistrar;
import org.spongepowered.api.command.registrar.CommandRegistrarType;
import org.spongepowered.plugin.PluginContainer;

@ApiStatus.Internal
public class LiteSpongeCommandRegistrarType implements CommandRegistrarType<LiteSpongeCommand>{

    private final PluginContainer plugin;

    public LiteSpongeCommandRegistrarType(PluginContainer plugin) {
        this.plugin = plugin;
    }

    @Override
    public TypeToken<LiteSpongeCommand> handledType() {
        return TypeToken.get(LiteSpongeCommand.class);
    }

    @Override
    public CommandRegistrar<LiteSpongeCommand> create(CommandManager.Mutable commandManager) {
        return new LiteSpongeCommandRegistrar(this, plugin, commandManager);
    }
}
