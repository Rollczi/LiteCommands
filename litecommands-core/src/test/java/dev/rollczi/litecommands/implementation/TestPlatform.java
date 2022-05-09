package dev.rollczi.litecommands.implementation;

import dev.rollczi.litecommands.command.FindResult;
import dev.rollczi.litecommands.command.section.CommandSection;
import dev.rollczi.litecommands.command.ExecuteResult;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.platform.Completer;
import dev.rollczi.litecommands.platform.ExecuteListener;
import dev.rollczi.litecommands.platform.RegistryPlatform;

import java.util.HashMap;
import java.util.Map;

public class TestPlatform implements RegistryPlatform<Void> {

    private final Map<CommandSection, Command> commands = new HashMap<>();

    @Override
    public void registerListener(CommandSection command, ExecuteListener<Void> listener, Completer<Void> completer) {
        this.commands.put(command, new Command(listener, completer));
    }

    @Override
    public void unregisterListener(CommandSection command) {
        this.commands.remove(command);
    }

    @Override
    public void unregisterAll() {
        this.commands.clear();
    }

    public ExecuteResult execute(String command, String... args) {
        for (Map.Entry<CommandSection, Command> entry : commands.entrySet()) {
            CommandSection section = entry.getKey();
            Command cmd = entry.getValue();

            if (section.isSimilar(command)) {
                return cmd.getExecuteListener().execute(null, new LiteInvocation(new TestSender(), command, command, args));
            }
        }

        return ExecuteResult.failure();
    }

    public FindResult find(String command, String... args) {
        for (Map.Entry<CommandSection, Command> entry : commands.entrySet()) {
            CommandSection section = entry.getKey();

            LiteInvocation liteInvocation = new LiteInvocation(new TestSender(), command, command, args);

            if (section.isSimilar(command)) {
                return section.find(liteInvocation, 0, FindResult.none(liteInvocation));
            }
        }

        throw new IllegalArgumentException();
    }

    private static final class Command {

        private final ExecuteListener<Void> executeListener;
        private final Completer<Void> completer;

        public Command(ExecuteListener<Void> executeListener, Completer<Void> completer) {
            this.executeListener = executeListener;
            this.completer = completer;
        }

        public ExecuteListener<Void> getExecuteListener() {
            return executeListener;
        }

        public Completer<Void> getCompletionListener() {
            return completer;
        }
    }

}
