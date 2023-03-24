package dev.rollczi.litecommands.bukkit;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.SimpleCommandMap;
import panda.std.Lazy;

import java.lang.reflect.Field;
import java.util.Map;

class BukkitCommandsProviderImpl implements BukkitCommandsProvider {

    private final Lazy<CommandMap> commandMap;
    private final Lazy<Map<String, Command>> knownCommands;

    private BukkitCommandsProviderImpl(Lazy<CommandMap> commandMap, Lazy<Map<String, Command>> knownCommands) {
        this.commandMap = commandMap;
        this.knownCommands = knownCommands;
    }

    @Override
    public CommandMap commandMap() {
        return this.commandMap.get();
    }

    @Override
    public Map<String, Command> knownCommands() {
        return this.knownCommands.get();
    }

    @SuppressWarnings("unchecked")
    static BukkitCommandsProvider create(Server server) {
        Lazy<CommandMap> commandMap = new Lazy<>(() -> {
            try {
                Field commandMapField = server.getClass().getDeclaredField("commandMap");

                commandMapField.setAccessible(true);
                return (CommandMap) commandMapField.get(server);
            } catch (NoSuchFieldException | IllegalAccessException exception) {
                throw new RuntimeException(exception);
            }
        });

        Lazy<Map<String, Command>> knownCommands = new Lazy<>(() -> {
            try {
                Field knownCommandMapField = SimpleCommandMap.class.getDeclaredField("knownCommands");
                knownCommandMapField.setAccessible(true);

                return (Map<String, Command>) knownCommandMapField.get(commandMap.get());
            }
            catch (IllegalAccessException | NoSuchFieldException exception) {
                throw new RuntimeException(exception);
            }
        });

        return new BukkitCommandsProviderImpl(commandMap, knownCommands);
    }

}
