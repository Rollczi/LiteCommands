package dev.rollczi.litecommands.test;

import dev.rollczi.litecommands.command.FindResult;
import dev.rollczi.litecommands.command.Invocation;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.command.execute.ExecuteResult;
import dev.rollczi.litecommands.command.section.CommandSection;
import dev.rollczi.litecommands.platform.ExecuteListener;
import dev.rollczi.litecommands.platform.LiteSender;
import dev.rollczi.litecommands.platform.RegistryPlatform;
import dev.rollczi.litecommands.platform.SuggestionListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class TestPlatform implements RegistryPlatform<TestHandle> {

    private final Map<CommandSection<TestHandle>, Command> commands = new HashMap<>();

    @Override
    public void registerListener(CommandSection<TestHandle> command, ExecuteListener<TestHandle> listener, SuggestionListener<TestHandle> suggestionListener) {
        this.commands.put(command, new Command(listener, suggestionListener));
    }

    @Override
    public void unregisterListener(CommandSection<TestHandle> command) {
        this.commands.remove(command);
    }

    @Override
    public void unregisterAll() {
        this.commands.clear();
    }

    public LiteSender createSender() {
        return new TestSender(new TestHandle(), false);
    }

    public AssertResult execute(String command, String... args) {
        TestHandle handle = new TestHandle();
        LiteInvocation invocation = new LiteInvocation(new TestSender(handle, false), command, command, args);

        for (Map.Entry<CommandSection<TestHandle>, Command> entry : commands.entrySet()) {
            CommandSection<TestHandle> section = entry.getKey();
            Command cmd = entry.getValue();

            if (section.isSimilar(command)) {
                return new AssertResult(cmd.getExecuteListener().execute(handle, invocation));
            }
        }

        return new AssertResult(ExecuteResult.failure(FindResult.none(invocation.withHandle(handle))));
    }

    public AssertSuggest suggestAsOp(String command, String... args) {
        TestHandle handle = new TestHandle();
        return suggest(new TestSender(handle, true), command, args);
    }

    public AssertSuggest suggest(LiteSender liteSender, String command, String... args) {
        for (Map.Entry<CommandSection<TestHandle>, Command> entry : commands.entrySet()) {
            CommandSection<TestHandle> section = entry.getKey();

            Invocation<TestHandle> liteInvocation = new Invocation<>((TestHandle) liteSender.getHandle(), liteSender, command, command, args);

            if (section.isSimilar(command)) {
                return new AssertSuggest(section.findSuggestion(liteInvocation, 0).merge());
            }
        }

        throw new IllegalArgumentException();
    }

    public FindResult<TestHandle> find(String command, String... args) {
        TestHandle handle = new TestHandle();
        for (Map.Entry<CommandSection<TestHandle>, Command> entry : commands.entrySet()) {
            CommandSection<TestHandle> section = entry.getKey();

            Invocation<TestHandle> liteInvocation = new Invocation<>(handle, new TestSender(handle, false), command, command, args);

            if (section.isSimilar(command)) {
                return section.find(liteInvocation.toLite(), 0, FindResult.none(liteInvocation));
            }
        }

        throw new IllegalArgumentException();
    }

    public List<String> suggestionAsOp(String command, String... args) {
        TestHandle handle = new TestHandle();
        for (Map.Entry<CommandSection<TestHandle>, Command> entry : commands.entrySet()) {
            CommandSection<TestHandle> section = entry.getKey();

            Invocation<TestHandle> liteInvocation = new Invocation<>(handle, new TestSender(handle, true), command, command, args);

            if (section.isSimilar(command)) {
                return section.findSuggestion(liteInvocation, 0).merge().multilevelSuggestions();
            }
        }

        throw new IllegalArgumentException();
    }

    private static final class Command {

        private final ExecuteListener<TestHandle> executeListener;
        private final SuggestionListener<TestHandle> suggestionListener;

        public Command(ExecuteListener<TestHandle> executeListener, SuggestionListener<TestHandle> suggestionListener) {
            this.executeListener = executeListener;
            this.suggestionListener = suggestionListener;
        }

        public ExecuteListener<TestHandle> getExecuteListener() {
            return executeListener;
        }

        public SuggestionListener<TestHandle> getSuggester() {
            return suggestionListener;
        }

    }

    public CommandSection<TestHandle> getSection(String key) {
        for (CommandSection<TestHandle> section : commands.keySet()) {
            if (section.isSimilar(key)) {
                return section;
            }
        }

        throw new NoSuchElementException();
    }

}
