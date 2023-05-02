package dev.rollczi.litecommands.unit;

import dev.rollczi.litecommands.command.CommandRoute;
import dev.rollczi.litecommands.argument.input.InputArguments;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.invocation.InvocationResult;
import dev.rollczi.litecommands.platform.Platform;
import dev.rollczi.litecommands.platform.PlatformInvocationHook;
import dev.rollczi.litecommands.platform.PlatformSuggestionHook;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public class TestPlatform implements Platform<TestSender, TestSettings> {

    private final Map<CommandRoute<TestSender>, PlatformInvocationHook<TestSender>> executeListeners = new LinkedHashMap<>();
    private final Map<CommandRoute<TestSender>, PlatformSuggestionHook<TestSender>> suggestListeners = new LinkedHashMap<>();
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
    public void register(CommandRoute<TestSender> commandRoute, PlatformInvocationHook<TestSender> invocationHook, PlatformSuggestionHook<TestSender> suggestionHook) {
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

        return this.execute(label, args);
    }

    public AssertExecute execute(String command, String... arguments) {
        TestSender testSender = new TestSender();
        TestPlatformSender testPlatformSender = new TestPlatformSender();

        InputArguments<?> args = InputArguments.raw(arguments);

        Invocation<TestSender> invocation = new Invocation<>(testSender, testPlatformSender, command, command, args);

        for (Map.Entry<CommandRoute<TestSender>, PlatformInvocationHook<TestSender>> entry : this.executeListeners.entrySet()) {
            if (entry.getKey().isNameOrAlias(command)) {
                PlatformInvocationHook<TestSender> listener = entry.getValue();
                InvocationResult<TestSender> result = listener.execute(invocation);

                return new AssertExecute(result);
            }
        }

        throw new IllegalStateException("No command found for " + command);
    }


    public void suggest(String command) {
        TestSender testSender = new TestSender();
        TestPlatformSender testPlatformSender = new TestPlatformSender();
        String label = command.split(" ")[0];
        String[] args = command.substring(label.length()).split(" ");

        InputArguments<?> arguments = InputArguments.raw(args);

        Invocation<TestSender> invocation = new Invocation<>(testSender, testPlatformSender, label, label, arguments);

        // TODO: implement
    }

}
