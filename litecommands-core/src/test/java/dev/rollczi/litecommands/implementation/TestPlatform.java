package dev.rollczi.litecommands.implementation;

import dev.rollczi.litecommands.command.FindResult;
import dev.rollczi.litecommands.command.sugesstion.SuggestResult;
import dev.rollczi.litecommands.command.section.CommandSection;
import dev.rollczi.litecommands.command.execute.ExecuteResult;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.platform.SuggestionListener;
import dev.rollczi.litecommands.platform.ExecuteListener;
import dev.rollczi.litecommands.platform.RegistryPlatform;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestPlatform implements RegistryPlatform<Void> {

    private final Map<CommandSection, Command> commands = new HashMap<>();

    @Override
    public void registerListener(CommandSection command, ExecuteListener<Void> listener, SuggestionListener<Void> suggestionListener) {
        this.commands.put(command, new Command(listener, suggestionListener));
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
        LiteInvocation invocation = new LiteInvocation(new TestSender(), command, command, args);

        for (Map.Entry<CommandSection, Command> entry : commands.entrySet()) {
            CommandSection section = entry.getKey();
            Command cmd = entry.getValue();

            if (section.isSimilar(command)) {
                return cmd.getExecuteListener().execute(null, invocation);
            }
        }

        return ExecuteResult.failure(FindResult.none(invocation));
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

    public List<String> suggestion(String command, String... args) {
        FindResult findResult = this.find(command, args);
        SuggestResult result = SuggestResult.of(findResult.knownSuggestion());

        return result.singleSuggestion();
    }

    private static final class Command {

        private final ExecuteListener<Void> executeListener;
        private final SuggestionListener<Void> suggestionListener;

        public Command(ExecuteListener<Void> executeListener, SuggestionListener<Void> suggestionListener) {
            this.executeListener = executeListener;
            this.suggestionListener = suggestionListener;
        }

        public ExecuteListener<Void> getExecuteListener() {
            return executeListener;
        }

        public SuggestionListener<Void> getSuggester() {
            return suggestionListener;
        }

    }

}