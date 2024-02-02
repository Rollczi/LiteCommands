package dev.rollczi.litecommands.bukkit;

import dev.rollczi.litecommands.input.raw.RawCommand;
import dev.rollczi.litecommands.reflect.LiteCommandsReflectException;
import dev.rollczi.litecommands.scheduler.Scheduler;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public abstract class TabComplete {

    private final static String FALLBACK_SEPARATOR = ":";

    protected Map<String, BukkitCommand> listeners = new HashMap<>();

    void register(String fallbackPrefix, BukkitCommand command) {
        for (String alias : command.getAliases()) {
            listeners.put(alias, command);
            listeners.put(fallbackPrefix + FALLBACK_SEPARATOR + alias, command);
        }

        listeners.put(command.getName(), command);
        listeners.put(fallbackPrefix + FALLBACK_SEPARATOR + command.getName(), command);
    }

    void unregister(String commandName) {
        listeners.remove(commandName);
    }

    void unregisterAll() {
        HashSet<String> set = new HashSet<>(this.listeners.keySet());

        for (String commandName : set) {
            this.unregister(commandName);
        }
    }

    void close() {
        this.unregisterAll();
    }

    @Nullable
    protected List<String> callListener(CommandSender sender, String buffer) {
        if (!buffer.startsWith(RawCommand.COMMAND_SLASH)) {
            return null;
        }

        RawCommand rawCommand = RawCommand.from(buffer);
        String commandName = rawCommand.getLabel();
        BukkitCommand command = listeners.get(commandName);

        if (command == null) {
            return null;
        }

        try {
            return command.suggest(sender, commandName, rawCommand.getArgs().toArray(new String[0]))
                .get(15, TimeUnit.SECONDS);
        }
        catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    static TabComplete create(Scheduler scheduler, Plugin plugin) {
        try {
            Class.forName("com.destroystokyo.paper.event.server.AsyncTabCompleteEvent");
            return new TabCompletePaperAsync(plugin);
        }
        catch (ClassNotFoundException ignored) {}

        try {
            Class.forName("com.comphenix.protocol.ProtocolLibrary");
            Class.forName("org.bukkit.craftbukkit.libs.jline.console.ConsoleReader");
            return new TabCompleteProtocolLibAsync(plugin, scheduler);
        }
        catch (ClassNotFoundException | LiteCommandsReflectException ignored) {}

        return new TabCompleteSync();
    }

}
