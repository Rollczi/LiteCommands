package dev.rollczi.litecommands.unit;

import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.argument.input.InputArguments;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.invocation.InvocationResult;
import dev.rollczi.litecommands.platform.Platform;
import dev.rollczi.litecommands.platform.PlatformInvocationListener;
import dev.rollczi.litecommands.platform.PlatformSuggestionListener;
import dev.rollczi.litecommands.suggestion.input.SuggestionInput;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;

public class TestPlatform implements Platform<TestSender, TestSettings> {

    private final Map<CommandRoute<TestSender>, PlatformInvocationListener<TestSender>> executeListeners = new LinkedHashMap<>();
    private final Map<CommandRoute<TestSender>, PlatformSuggestionListener<TestSender>> suggestListeners = new LinkedHashMap<>();
    private TestSettings configuration = new TestSettings();

    @Override
    public void setConfiguration(@NotNull TestSettings liteConfiguration) {
        this.configuration = liteConfiguration;
    }

    @Override
    public @NotNull TestSettings getConfiguration() {
        return configuration;
    }

    @Override
    public void register(CommandRoute<TestSender> commandRoute, PlatformInvocationListener<TestSender> invocationHook, PlatformSuggestionListener<TestSender> suggestionHook) {
        this.executeListeners.put(commandRoute, invocationHook);
        this.suggestListeners.put(commandRoute, suggestionHook);
    }

    @Override
    public void unregister(CommandRoute<TestSender> commandRoute) {
        this.executeListeners.remove(commandRoute);
        this.suggestListeners.remove(commandRoute);
    }

    @Override
    public void unregisterAll() {
        this.executeListeners.clear();
        this.suggestListeners.clear();
    }

    public AssertExecute execute(String command) {
        String label = command.split(" ")[0];
        String[] args = command.length() > label.length()
            ? command.substring(label.length() + 1).split(" ")
            : new String[0];

        if (command.charAt(command.length() - 1) == ' ') {
            args = new String[args.length + 1];
            args[args.length - 1] = "";
        }

        return this.execute(label, args);
    }

    public AssertExecute execute(String command, String... arguments) {
        TestSender testSender = new TestSender();
        TestPlatformSender testPlatformSender = new TestPlatformSender();

        InputArguments<?> args = InputArguments.raw(arguments);

        Invocation<TestSender> invocation = new Invocation<>(testSender, testPlatformSender, command, command, args);

        for (Map.Entry<CommandRoute<TestSender>, PlatformInvocationListener<TestSender>> entry : this.executeListeners.entrySet()) {
            if (entry.getKey().isNameOrAlias(command)) {
                PlatformInvocationListener<TestSender> listener = entry.getValue();
                InvocationResult<TestSender> result = listener.execute(invocation, InputArguments.raw(arguments));

                return new AssertExecute(result);
            }
        }

        throw new IllegalStateException("No command found for " + command);
    }


    public AssertSuggest suggest(String command) {
        TestSender testSender = new TestSender();
        TestPlatformSender testPlatformSender = new TestPlatformSender();
        String label = command.split(" ")[0];
        String[] args = command.length() > label.length()
            ? command.substring(label.length() + 1).split(" ")
            : new String[0];

        if (command.charAt(command.length() - 1) == ' ' && !args[args.length - 1].isEmpty()) {
            String[] temp = new String[args.length + 1];
            System.arraycopy(args, 0, temp, 0, args.length);
            temp[args.length] = "";

            args = temp;
        }

        SuggestionInput<?> arguments = SuggestionInput.raw(args);
        Invocation<TestSender> invocation = new Invocation<>(testSender, testPlatformSender, label, label, arguments);

        for (Map.Entry<CommandRoute<TestSender>, PlatformSuggestionListener<TestSender>> entry : this.suggestListeners.entrySet()) {
            if (entry.getKey().isNameOrAlias(label)) {
                PlatformSuggestionListener<TestSender> listener = entry.getValue();
                return new AssertSuggest(listener.suggest(invocation, arguments));
            }
        }

        throw new IllegalStateException("No command found for " + command);
    }

}
