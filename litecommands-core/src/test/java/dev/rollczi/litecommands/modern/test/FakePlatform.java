package dev.rollczi.litecommands.modern.test;

import dev.rollczi.litecommands.modern.command.CommandRoute;
import dev.rollczi.litecommands.modern.invocation.Invocation;
import dev.rollczi.litecommands.modern.invocation.InvocationResult;
import dev.rollczi.litecommands.modern.platform.Platform;
import dev.rollczi.litecommands.modern.platform.PlatformInvocationHook;
import dev.rollczi.litecommands.modern.platform.PlatformSuggestionHook;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;

public class FakePlatform implements Platform<FakeSender, FakeConfig> {

    private final Map<CommandRoute<FakeSender>, PlatformInvocationHook<FakeSender>> executeListeners = new LinkedHashMap<>();
    private final Map<CommandRoute<FakeSender>, PlatformSuggestionHook<FakeSender>> suggestListeners = new LinkedHashMap<>();
    private FakeConfig configuration = new FakeConfig();

    @Override
    public void setConfiguration(@NotNull FakeConfig liteConfiguration) {
        this.configuration = liteConfiguration;
    }

    @Override
    public @NotNull FakeConfig getConfiguration() {
        return configuration;
    }

    @Override
    public void register(CommandRoute<FakeSender> commandRoute, PlatformInvocationHook<FakeSender> invocationHook, PlatformSuggestionHook<FakeSender> suggestionHook) {
        this.executeListeners.put(commandRoute, invocationHook);
        this.suggestListeners.put(commandRoute, suggestionHook);
    }

    @Override
    public void unregister(CommandRoute<FakeSender> commandRoute) {
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
        FakeSender fakeSender = new FakeSender();
        FakePlatformSender fakePlatformSender = new FakePlatformSender();

        Invocation<FakeSender> invocation = new Invocation<>(fakeSender, fakePlatformSender, command, command, arguments);

        for (Map.Entry<CommandRoute<FakeSender>, PlatformInvocationHook<FakeSender>> entry : this.executeListeners.entrySet()) {
            if (entry.getKey().isNameOrAlias(command)) {
                PlatformInvocationHook<FakeSender> listener = entry.getValue();
                InvocationResult<FakeSender> result = listener.execute(invocation);

                return new AssertExecute(result);
            }
        }

        throw new IllegalStateException("No command found for " + command);
    }


    public void suggest(String command) {
        FakeSender fakeSender = new FakeSender();
        FakePlatformSender fakePlatformSender = new FakePlatformSender();
        String label = command.split(" ")[0];
        String[] args = command.substring(label.length()).split(" ");

        Invocation<FakeSender> invocation = new Invocation<>(fakeSender, fakePlatformSender, label, label, args);




    }

}
