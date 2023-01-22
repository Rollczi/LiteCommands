package dev.rollczi.litecommands.minestom;

import dev.rollczi.litecommands.command.section.CommandSection;
import dev.rollczi.litecommands.platform.ExecuteListener;
import dev.rollczi.litecommands.platform.RegistryPlatform;
import dev.rollczi.litecommands.platform.SuggestionListener;
import net.minestom.server.command.CommandManager;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;

import java.util.HashSet;
import java.util.Set;

class LiteMinestomRegistryPlatform implements RegistryPlatform<CommandSender> {

    private final Set<String> commands = new HashSet<>();

    private final CommandManager commandManager;

    LiteMinestomRegistryPlatform(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    @Override
    public void registerListener(CommandSection<CommandSender> command, ExecuteListener<CommandSender> executeListener, SuggestionListener<CommandSender> suggestionListener) {
        SimpleCommand minestomSimpleCommand = this.createCommand(command, executeListener, suggestionListener);

        this.commandManager.register(minestomSimpleCommand);
        this.commands.add(command.getName());
        this.commands.addAll(command.getAliases());
    }

    private SimpleCommand createCommand(CommandSection<CommandSender> command, ExecuteListener<CommandSender> executeListener, SuggestionListener<CommandSender> suggestionListener) {
        return new SimpleCommand(command, executeListener, suggestionListener);
    }

    @Override
    public void unregisterListener(CommandSection<CommandSender> command) {
        Command commandToDelete = this.commandManager.getCommand(command.getName());

        if (commandToDelete == null) {
            return;
        }

        this.commandManager.unregister(commandToDelete);
        this.commands.remove(command.getName());
    }

    @Override
    public void unregisterAll() {
        for (String command : this.commands) {
            Command commandToDelete = this.commandManager.getCommand(command);

            if (commandToDelete != null) {
                this.commandManager.unregister(commandToDelete);
            }
        }

        this.commands.clear();
    }

}
