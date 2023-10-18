package dev.rollczi.litecommands.sponge.internal;

import org.spongepowered.api.command.manager.CommandMapping;
import org.spongepowered.api.command.registrar.CommandRegistrar;
import org.spongepowered.plugin.PluginContainer;

import java.util.Optional;
import java.util.Set;

public class LiteSpongeCommandMapping implements CommandMapping {

    private final PluginContainer plugin;
    private final LiteSpongeCommandRegistrar commandRegistrar;
    private final LiteSpongeCommand command;

    public LiteSpongeCommandMapping(PluginContainer plugin, LiteSpongeCommandRegistrar commandRegistrar, LiteSpongeCommand command) {
        this.plugin = plugin;
        this.commandRegistrar = commandRegistrar;
        this.command = command;
    }

    @Override
    public String primaryAlias() {
        return command.getLabel();
    }

    @Override
    public Set<String> allAliases() {
        return command.getAllAliases();
    }

    @Override
    public Optional<PluginContainer> plugin() {
        return Optional.of(plugin);
    }

    @Override
    public CommandRegistrar<?> registrar() {
        return commandRegistrar;
    }

    public LiteSpongeCommand getCommand() {
        return command;
    }
}
