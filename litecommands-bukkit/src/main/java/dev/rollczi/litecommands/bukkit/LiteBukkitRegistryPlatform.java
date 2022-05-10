package dev.rollczi.litecommands.bukkit;

import dev.rollczi.litecommands.command.section.CommandSection;
import dev.rollczi.litecommands.platform.Suggester;
import dev.rollczi.litecommands.platform.ExecuteListener;
import dev.rollczi.litecommands.platform.RegistryPlatform;
import org.bukkit.Server;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class LiteBukkitRegistryPlatform implements RegistryPlatform<CommandSender> {

    private final Set<String> commands = new HashSet<>();

    private final CommandMap commandMap;
    private final Map<String, org.bukkit.command.Command> knownCommands;
    private final String fallbackPrefix;

    @SuppressWarnings("unchecked")
    public LiteBukkitRegistryPlatform(Server server, String fallbackPrefix) {
        this.fallbackPrefix = fallbackPrefix;

        try {
            Field commandMapField = server.getClass().getDeclaredField("commandMap");

            commandMapField.setAccessible(true);
            this.commandMap = (CommandMap) commandMapField.get(server);

            Field knownCommandMapField = SimpleCommandMap.class.getDeclaredField("knownCommands");
            knownCommandMapField.setAccessible(true);

            this.knownCommands = (Map<String, org.bukkit.command.Command>) knownCommandMapField.get(this.commandMap);

        } catch (NoSuchFieldException | IllegalAccessException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void registerListener(CommandSection command, ExecuteListener<CommandSender> executeListener, Suggester<CommandSender> suggester) {
        SimpleCommand bukkitSimpleCommand = new SimpleCommand(command, executeListener, suggester);

        this.commandMap.register(command.getName(), this.fallbackPrefix, bukkitSimpleCommand);
        this.commands.add(command.getName());
        this.commands.addAll(command.getAliases());
    }

    @Override
    public void unregisterListener(CommandSection command) {
        this.knownCommands.remove(command.getName());
        this.commands.remove(command.getName());

        for (String alias : command.getAliases()) {
            this.knownCommands.remove(alias);
            this.commands.remove(alias);
        }
    }

    @Override
    public void unregisterAll() {
        for (String command : this.commands) {
            this.knownCommands.remove(command);
        }

        this.commands.clear();
    }

}
