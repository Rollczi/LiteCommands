package dev.rollczi.litecommands.bukkit;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.SimpleCommandMap;
import org.jetbrains.annotations.NotNull;
import panda.std.Lazy;

import java.lang.reflect.Field;
import java.util.Map;

class BukkitCommandsRegistryImpl implements BukkitCommandsRegistry {

    private final Lazy<CommandMap> commandMap;
    private final Lazy<Map<String, Command>> knownCommands;

    private BukkitCommandsRegistryImpl(Lazy<CommandMap> commandMap, Lazy<Map<String, Command>> knownCommands) {
        this.commandMap = commandMap;
        this.knownCommands = knownCommands;
    }

    @SuppressWarnings("unchecked")
    static BukkitCommandsRegistry create(Server server) {
        Lazy<CommandMap> commandMap = new Lazy<>(() -> {
            try {
                Field commandMapField = server.getClass().getDeclaredField("commandMap");

                commandMapField.setAccessible(true);
                return (CommandMap) commandMapField.get(server);
            }
            catch (NoSuchFieldException | IllegalAccessException exception) {
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

        return new BukkitCommandsRegistryImpl(commandMap, knownCommands);
    }

    @Override
    public boolean register(@NotNull String label, @NotNull String fallbackPrefix, @NotNull Command command) {
        return this.commandMap.get().register(label, fallbackPrefix, command);
    }

    @Override
    public void unregister(@NotNull String label, String fallbackPrefix) {
        this.knownCommands.get().remove(label);
        this.knownCommands.get().remove(fallbackPrefix + ":" + label);
    }

}
