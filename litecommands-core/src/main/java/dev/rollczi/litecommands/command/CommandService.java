package dev.rollczi.litecommands.command;

import dev.rollczi.litecommands.command.execute.ExecuteResult;
import dev.rollczi.litecommands.command.section.CommandSection;
import dev.rollczi.litecommands.handle.ExecuteResultHandler;
import dev.rollczi.litecommands.platform.RegistryPlatform;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CommandService<SENDER> {

    private final RegistryPlatform<SENDER> platform;
    private final Map<String, CommandSection<SENDER>> commands = new HashMap<>();
    private final ExecuteResultHandler<SENDER> handler;

    public CommandService(RegistryPlatform<SENDER> platform, ExecuteResultHandler<SENDER> handler) {
        this.platform = platform;
        this.handler = handler;
    }

    public CommandSection<SENDER> getSection(String key) {
        return this.commands.get(key);
    }

    public Collection<CommandSection<SENDER>> getSections() {
        return Collections.unmodifiableCollection(this.commands.values());
    }

    public void register(CommandSection<SENDER> section) {
        commands.put(section.getName(), section);

        for (String label : section.getAliases()) {
            commands.put(label, section);
        }

        platform.registerListener(
                section,
                (sender, invocation) -> {
                    ExecuteResult result = section.execute(invocation.withHandle(sender));

                    this.handler.handle(sender, invocation, result);
                    return result;
                },
                (sender, invocation) -> section.suggestion(invocation.withHandle(sender))
        );
    }

    public ExecuteResultHandler<SENDER> getHandler() {
        return handler;
    }

    public RegistryPlatform<SENDER> getPlatform() {
        return platform;
    }

}
