package dev.rollczi.litecommands.sponge.internal;

import dev.rollczi.litecommands.input.raw.RawCommand;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.ApiStatus;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.CommandCompletion;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.manager.CommandFailedRegistrationException;
import org.spongepowered.api.command.manager.CommandManager;
import org.spongepowered.api.command.manager.CommandMapping;
import org.spongepowered.api.command.registrar.CommandRegistrar;
import org.spongepowered.api.command.registrar.CommandRegistrarType;
import org.spongepowered.api.command.registrar.tree.CommandTreeNode;
import org.spongepowered.plugin.PluginContainer;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@SuppressWarnings("Convert2MethodRef")
@ApiStatus.Internal
public class LiteSpongeCommandRegistrar implements CommandRegistrar<LiteSpongeCommand> {

    private final CommandRegistrarType<LiteSpongeCommand> type;
    private final PluginContainer plugin;
    private final CommandManager.Mutable commandManager;

    public LiteSpongeCommandRegistrar(CommandRegistrarType<LiteSpongeCommand> type, PluginContainer plugin, CommandManager.Mutable commandManager) {
        this.type = type;
        this.plugin = plugin;
        this.commandManager = commandManager;
    }

    @Override
    public CommandRegistrarType<LiteSpongeCommand> type() {
        return type;
    }

    @Override
    public CommandMapping register(PluginContainer container, LiteSpongeCommand command, String primaryAlias, String... secondaryAliases) throws CommandFailedRegistrationException {
        commandManager.registerAlias(
            this,
            plugin,
            CommandTreeNode.root().executable(),
            primaryAlias,
            secondaryAliases
        );

        return new LiteSpongeCommandMapping(plugin, this, command);
    }

    @Override
    public CommandResult process(CommandCause cause, CommandMapping mapping, String command, String arguments) throws CommandException {
        try {
            asLiteMapping(mapping).getCommand().execute(cause, command, arguments.split(RawCommand.COMMAND_SEPARATOR));
            return CommandResult.success();
        } catch (Throwable e) {
            throw new CommandException(Component.empty(), e);
        }
    }

    @Override
    public List<CommandCompletion> complete(CommandCause cause, CommandMapping mapping, String command, String arguments) throws CommandException {
        try {
            return asLiteMapping(mapping).getCommand().suggest(cause, command, arguments.split(RawCommand.COMMAND_SEPARATOR))
                .stream()
                .map(suggestion -> CommandCompletion.of(suggestion))
                .collect(Collectors.toList());
        } catch (Throwable e) {
            throw new CommandException(Component.empty(), e);
        }
    }

    @Override
    public Optional<Component> help(CommandCause cause, CommandMapping mapping) {
        return Optional.empty();
    }

    @Override
    public boolean canExecute(CommandCause cause, CommandMapping mapping) {
        return asLiteMapping(mapping).getCommand().canExecute(cause);
    }

    private LiteSpongeCommandMapping asLiteMapping(CommandMapping mapping) {
        if (!(mapping instanceof LiteSpongeCommandMapping)) {
            throw new IllegalArgumentException("Must receive only LiteSpongeCommandMapping but received " + mapping.getClass().getName());
        }

        return (LiteSpongeCommandMapping) mapping;
    }
}
