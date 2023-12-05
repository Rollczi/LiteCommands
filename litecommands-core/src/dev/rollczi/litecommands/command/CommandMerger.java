package dev.rollczi.litecommands.command;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CommandMerger<SENDER> {

    private final Map<String, CommandRoute<SENDER>> commandsByName = new HashMap<>();
    private final Map<UUID, CommandRoute<SENDER>> commands = new HashMap<>();

    public void merge(CommandRoute<SENDER> current) {
        for (String name : current.names()) {
            if (!commandsByName.containsKey(name)) {
                continue;
            }

            CommandRoute<SENDER> toRemove = commandsByName.get(name);
            CommandRoute<SENDER> mergedCommand = toRemove.merge(current);

            for (String commandName : mergedCommand.names()) {
                commandsByName.put(commandName, mergedCommand);
            }

            commands.remove(toRemove.getUniqueId());
            commands.put(mergedCommand.getUniqueId(), mergedCommand);
            return;
        }

        for (String name : current.names()) {
            commandsByName.put(name, current);
        }

        commands.put(current.getUniqueId(), current);
    }

    public Collection<CommandRoute<SENDER>> getMergedCommands() {
        return Collections.unmodifiableCollection(commands.values());
    }

}
